# Gameplay config is server-owned

Growth speed, Mark duration, teleport cooldown, and Presence Range change the rules of the world. They live in `config/echo_seed.json` on the server and are synced to clients.

YACL is an optional editor, not the source of truth. The JSON is still loaded when YACL is missing. This is deliberate: Better-Client can ignore its file without YACL because it is client-only; Echo Seed cannot.
