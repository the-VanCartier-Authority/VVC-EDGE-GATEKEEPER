# Informe de recuperación y estabilización

**Proyecto:** VVC-EDGE-GATEKEEPER  
**Fecha:** 21 de agosto de 2026  
**Estado integrado:** `main`  
**Commit funcional de código:** `318a6df` (`chore: remove unused compose viewmodel dependency`)

**HEAD actual de `main`:** `8ff147b` (`docs: align recovery artifact hashes`)

## Estado final

El proyecto queda **compilable de forma reproducible y con el núcleo de verificación endurecido**. La rama de trabajo fue integrada en `main` después de pasar Android CI en la pull request [#3][2] y nuevamente en el commit final mediante la ejecución [32493925317][1].

La validación de instalación, lanzamiento y flujo biométrico en un dispositivo físico no queda marcada como completada. El entorno de ejecución no expone `/dev/kvm`, por lo que el emulador x86_64 no puede arrancar; además, `adb devices -l` no muestra ningún dispositivo conectado. Esta limitación está comprobada en `/home/ubuntu/vvc_emulator.log` y no se convirtió en una afirmación de funcionamiento basada únicamente en análisis estático.

## Línea base y diagnóstico confirmado

La revisión partió de `main` en `eddad30` y de todas las ramas remotas disponibles. No se encontraron commits u objetos no referenciados que contuvieran una implementación anterior de captura facial. El proyecto tenía una única aplicación Android, Gradle 8.5, Android Gradle Plugin 8.2.2, Kotlin 1.9.22, `compileSdk` 34, `minSdk` 26 y `targetSdk` 34.

El primer build local falló por ausencia del Android SDK, no por un error de código: Gradle informó `SDK location not found`. Se instaló el SDK oficial requerido en `/home/ubuntu/android-sdk`; después, el proyecto compiló. La causa de los problemas de calidad funcional confirmados en el código era distinta: el modelo aceptaba arrays de cualquier longitud, el almacenamiento podía persistir vectores no válidos y el parseo podía eliminar tokens corruptos silenciosamente. Asimismo, la autenticación facial end-to-end no está implementada en el repositorio actual: no existen `FaceCaptureSource.kt`, dependencias CameraX/ML Kit, extracción de embeddings de 128 posiciones ni conexión de `AuthViewModel` con una captura real.

| Elemento | Evidencia final |
|---|---|
| Rama base | `main` en `eddad30` |
| Rama de recuperación | `recovery/stabilize-functional-2026-08-21` |
| Pull request | [#3][2], integrada mediante squash |
| Commit integrado inicial | `dd6d0cf` |
| Commit funcional de código | `318a6df` |
| HEAD actual de `main` | `8ff147b` |
| Estado del working tree | Limpio y sincronizado con `origin/main` |

## Reparaciones aplicadas

Se conservaron la arquitectura y las dependencias existentes salvo los cambios estrictamente necesarios para proteger el flujo actual. No se modificaron `applicationId`, package, identidad de marca, recursos visuales ni la lógica declarada de distancia euclidiana.

| Archivo o área | Cambio realizado | Resultado verificable |
|---|---|---|
| `FaceVector.kt` | Exige exactamente 128 valores finitos y copia la entrada mediante `FaceVector.from`. | El dominio rechaza dimensiones incorrectas, `NaN` e infinitos. |
| `VerifyFaceVectorUseCase.kt` | Mantiene `Dispatchers.Default`, usa acumulación `Double` y expone el umbral `0.4f`. | La comparación sigue siendo euclidiana y el límite está probado. |
| `EncryptedStorage.kt` | Valida al guardar y devuelve `null` ante cualquier registro persistido inválido o corrupto. | No se aceptan vectores parciales ni tokens inválidos silenciosamente. |
| `AndroidManifest.xml` | Conecta reglas de backup Android 12+ y versiones anteriores; usa `@string/app_name`. | El vector biométrico cifrado queda excluido de backups. |
| `res/xml/backup_rules.xml` | Excluye `vvc_gatekeeper_secure_prefs.xml` en backups antiguos. | Se evita exportar el almacenamiento sensible. |
| `res/xml/data_extraction_rules.xml` | Excluye el mismo archivo de cloud backup y device transfer. | Se conserva la exclusión en Android 12+. |
| `app/src/test` | Añade 8 pruebas unitarias del modelo y del caso de uso. | Se comprueban aceptación, umbral, rechazo, dimensión y finitud. |

## Validación ejecutada

La validación local se ejecutó con el ciclo de limpieza, pruebas, lint y empaquetado. Tanto la variante debug como la release unsigned generaron artefactos válidos.

| Verificación | Resultado | Evidencia |
|---|---:|---|
| `./gradlew clean testDebugUnitTest lintDebug assembleDebug assembleRelease` | Éxito | `/home/ubuntu/vvc_final_build.log`, `BUILD SUCCESSFUL` |
| Pruebas unitarias | 8/8, 0 fallos, 0 errores, 0 omitidas | `app/build/test-results/testDebugUnitTest/*.xml` |
| `lintDebug` | Éxito sin errores | `app/build/reports/lint-results-debug.xml` |
| `assembleDebug` | Éxito | `app/build/outputs/apk/debug/app-debug.apk` |
| `assembleRelease` | Éxito | `app/build/outputs/apk/release/app-release-unsigned.apk` |
| Android CI sobre la rama de recuperación | Éxito | Run `32492539956` |
| Android CI sobre `main` integrado | Éxito | Run `32493060064` |
| Android CI sobre el commit funcional | Éxito | Run `32493417888` |
| Android CI sobre el HEAD actual de `main` | Éxito | Run `32493925317` |
| `git diff --check` | Sin errores de whitespace | Ejecutado antes de cada commit |

### Artefactos

| Artefacto | Tamaño | SHA-256 |
|---|---:|---|
| `app-debug.apk` | 7.6 MB | `410950393017c4c1c7cfdc7c9ac349049c91e8e28ef21989e4cb1dc688d02a49` |
| `app-release-unsigned.apk` | 5.7 MB | `fbc55cda9b4753cc3c3d0105553fca9b5210e211a832b53029bd956412e31315` |

## Advertencias pendientes no bloqueantes

Lint conserva seis advertencias de dependencias antiguas y una advertencia de forma del icono launcher. No se actualizaron dependencias de forma indiscriminada, porque el repositorio fija versiones conocidas y la compilación remota es verde. La advertencia del icono pertenece a la etapa visual y se dejó fuera de esta recuperación funcional, tal como exige el orden de prioridades.

La pipeline remota también informa que `actions/checkout@v4`, `actions/setup-java@v4` y `actions/upload-artifact@v4` están siendo ejecutadas bajo Node.js 24, y recomienda migrar `setup-java` a v5. Esto no impidió el build; queda registrado como mantenimiento posterior separado de la recuperación funcional.

## Validación de runtime y bloqueo externo

Se instaló un AVD API 34 y se intentó arrancarlo en modo headless. El arranque terminó con el error exacto `x86_64 emulation currently requires hardware acceleration` y `CPU acceleration status: /dev/kvm is not found`. La lista final de ADB fue `List of devices attached` sin dispositivos.

> **Estado de runtime:** APK generado y estructuralmente válido; instalación, lanzamiento y función biométrica end-to-end aún no verificables en este entorno porque falta un dispositivo Android o una máquina con aceleración de virtualización habilitada.

El repositorio tampoco contiene la implementación de captura y extracción que el README describe como arquitectura futura. Por tanto, el núcleo matemático y su persistencia están estabilizados, pero no se declara recuperada la autenticación facial completa hasta que exista esa integración y pueda ejecutarse en un dispositivo.

## Criterio de cierre

La aplicación queda **BUILD-STABLE / CORE-VALIDATED / RUNTIME-NOT-VERIFIED**. Los cambios están integrados en `main`, registrados en commits y validados por Android CI. La homogeneización visual no se inició; debe ejecutarse únicamente después de completar la integración real de captura facial y la prueba física de instalación, lanzamiento, navegación y función principal.

## Referencias

[1]: https://github.com/the-VanCartier-Authority/VVC-EDGE-GATEKEEPER/actions/runs/32493925317 "Android CI del HEAD final en main"
[2]: https://github.com/the-VanCartier-Authority/VVC-EDGE-GATEKEEPER/pull/3 "Pull request de recuperación funcional"
