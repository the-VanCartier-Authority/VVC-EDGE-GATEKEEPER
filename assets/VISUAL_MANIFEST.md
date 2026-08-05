# VVC EDGE CONTROL - VISUAL MANIFEST
> **Scope:** UI/UX Guidelines for VVC-EDGE CONTROL  Suite
> **Style:** Cyberpunk/Root Access meets Silent Luxury (Old Money Business)

## 1. Paleta de Colores (Hexadecimal)
Las interfaces de Jetpack Compose deben instanciar estrictamente estos valores dentro de `presentation/theme/Color.kt`:
- **Background/Surface (Capa 0):** `#0A0B0D` (Negro Profundo/Obsidiana, mate, sin reflejos).
- **Primary / Accent (Control):** `#00FF66` (Verde Terminal Matrix / Criptográfico Puro).
- **Secondary (Silent Luxury):** `#D4AF37` (Oro Antiguo / Textos de alta jerarquía o estatus de alerta segura).
- **Text Primary:** `#E5E7EB` (Gris Platino de alta legibilidad).
- **Text Secondary:** `#9CA3AF` (Gris Ceniza para metadatos y descripciones).

## 2. Tipografía y Comportamiento de Interfaz
- **Fuentes:** Monospace para datos de infraestructura (HLR, NPA/NXX, hashes); Sans-Serif limpia (tipo Inter/Roboto) para la navegación de la suite.
- **Bordes y Contenedores:** Ángulos rectos y afilados. Prohibidos los bordes redondeados orgánicos o infantiles. El uso de líneas finas de 1dp en color `#9CA3AF` delimita los bloques de datos.
- **Efectos:** Sin degradados llamativos. La estética emula una consola de comandos de alta gama o una suite corporativa de inteligencia privada de nivel soberano.

## 3. Implementación de Assets
- **`logo_suite.png`:** Debe usarse e⁷n la pantalla de inicio (Splash Screen) si existiera, o centrado en la parte superior del menú de bienvenida de la suite.
- **`logo_interfaz.png`:** Ubicado en la barra superior (*TopAppBar*) del Dashboard principal de `VVC-LOCKER-LIGHT` de forma sutil, alineado a la izquierda.
- **`ic_launcher.png`:** Icono del binario en el sistema operativo y sello de finalización de procesos.
- 
