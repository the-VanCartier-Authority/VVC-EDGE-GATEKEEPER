# VVC-EDGE-GATEKEEPER

## 1. PROPÓSITO DEL MÓDULO
Componente perimetral de seguridad biométrica local para la suite **VVC EDGE CONTROL**. Este módulo actúa como un guardián de acceso interno (*Gatekeeper*) encargado de validar la identidad del operador mediante reconocimiento facial antes de permitir la interacción con el Core de la suite.

---

## 2. ARQUITECTURA TÉCNICA OBLIGATORIA
El proyecto se rige estrictamente bajo **Clean Architecture** y **MVVM**. No se permiten desviaciones de esta estructura:

com.vvc.edge.gatekeeper/
├── data/                         # Capa de Datos (Cámara, ML Kit, Cifrado)
│   ├── datasource/
│   │   ├── FaceCaptureSource.kt  # Captura de frames estáticos (CameraX)
│   │   └── EncryptedStorage.kt   # Almacenamiento AES-256 (Jetpack Security)
│   └── repository/
│       └── AuthRepositoryImpl.kt # Coordinación de vectores de características
├── domain/                       # Capa de Negocio (Reglas puras, Casos de Uso)
│   ├── model/
│   │   └── FaceVector.kt         # Representación del embedding (128 flotantes)
│   └── usecase/
│       └── VerifyFaceVectorUseCase.kt # Cálculo de Distancia Euclidiana
└── presentation/                 # Capa de Interfaz de Usuario
    └── auth/
        ├── AuthState.kt          # Estados de UI (Idle, Loading, Success, Error)
        ├── AuthViewModel.kt      # Gestión de hilos (Coroutines)
        └── AuthScreen.kt         # UI Declarativa (Jetpack Compose)

---

## 3. ESPECIFICACIONES DE OPERACIÓN Y RENDIMIENTO (REGLAS PARA CODEX / MANUS)

### A. Procesamiento Asíncrono en Hilos Separados
* **Hilo de UI (`Dispatchers.Main`):** Exclusivo para el renderizado de Jetpack Compose y el flujo de vista previa de CameraX.
* **Hilo de Cómputo (`Dispatchers.Default`):** Obligatorio para la inicialización de ML Kit, la extracción del vector y el cálculo matemático de coincidencia. Ninguna operación matemática o de descifrado debe bloquear el hilo principal.

### B. Algoritmo de Verificación Biométrica
* **Prohibición:** Queda estrictamente prohibida la comparación píxel por píxel o el uso de hashes directos (`==`) sobre los datos crudos.
* **Mecanismo:** El sistema mapea el rostro en un **vector de características (embedding) de 128 posiciones (`FloatArray`)**.
* **Criterio de Aceptación:** La validación se ejecuta calculando la **Distancia Euclidiana** entre el vector capturado y el vector patrón encriptado localmente.
    * **Umbral Estricto:** Un resultado **$\le 0.4f$** se determina como coincidencia exitosa. Cualquier valor superior aborta el acceso.

---

## 4. GESTIÓN DE RECURSOS VISUALES (LOGOTIPOS E ICONOS)
Para mantener la consistencia de marca de la suite VVC, los archivos de imagen deben guardarse estrictamente en las siguientes rutas:

1. **Icono de la Aplicación (Launcher):**
   * **Ruta:** `app/src/main/res/mipmap-xhdpi/ic_launcher.png` (reemplazar el recurso nativo).
2. **Logotipo Interno de la UI:**
   * **Ruta:** `app/src/main/res/drawable/vvc_logo.png`
   * **Restricción de UI:** La pantalla `AuthScreen.kt` debe renderizar este logo en la parte superior del contenedor principal usando `painterResource(id = R.drawable.vvc_logo)`. Enfoque visual bajo estética Cyberpunk / Silent Luxury: fondo negro puro (#000000) y acentos en verde neón (#00FF00).

---

## 5. HOJA DE RUTA DE DESARROLLO (FASES)
* **Fase 1 (Actual):** Implementación de procesamiento asíncrono sobre imagen estática + verificación por distancia euclidiana de 128 bits.
* **Fase 2 (Siguiente):** Integración de análisis de prueba de vida (Liveness Detection) para mitigar spoofing por fotografía estática.
