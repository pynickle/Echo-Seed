# Echo Seed configuration is server-owned

Growth speed, Mark duration, teleport cooldown, Presence Range, and the Mark-duration action-bar display live in `config/echo_seed.json` on the server and are synced to clients. The first four change world rules; the display controls feedback while a live Mark exists.

YACL is an optional editor, not the source of truth. The JSON is still loaded when YACL is missing. This is deliberate: Better-Client can ignore its file without YACL because it is client-only; Echo Seed cannot.
