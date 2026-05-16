# Manolito Voice Android

Base inicial del proyecto Android para una app de voz moderna, premium y nativa.

## Qué trae ya
- Proyecto multi-módulo
- App Compose arrancable
- Tema oscuro base
- Pantalla Home inicial
- Orb visual reactiva por estados
- Navegación mínima
- Módulo `core:network`
- Módulo `core:audio`
- Contrato inicial Android ↔ backend para `/conversation/message`
- Grabación local base con permiso de micrófono
- Subida de audio al backend
- Campo `reply.audioUrl` preparado para TTS
- Reproducción remota base de TTS en Android

## Primeros siguientes pasos
1. Abrir en Android Studio
2. Sincronizar dependencias
3. Levantar el backend en `backend/`
4. Probar la llamada desde el emulador
5. Conectar STT real al audio grabado
6. Implementar streaming
