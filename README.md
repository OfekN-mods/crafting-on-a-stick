# Crafting On A Stick

[![CurseForge](https://img.shields.io/curseforge/dt/577850?label=CurseForge&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/crafting-on-a-stick)
[![Discord](https://img.shields.io/discord/943808282408128524?label=Discord&logo=discord)](https://discord.gg/eTn5zzrWKa)

## About

**Crafting on a Stick** is a Minecraft mod that adds portable versions of workbenches - use them directly from your inventory without needing a placed block, or even assign a keybind.

For full details, visit the [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/crafting-on-a-stick).
For questions or discussion, join the [Discord server](https://discord.gg/eTn5zzrWKa).

---

## Translations

Contributions for new language translations are welcome! You can either open a pull request or send your translation file in the [Discord server](https://discord.gg/eTn5zzrWKa).

The full English translation file can be found at [`en_us.json`](https://github.com/OfekN-mods/crafting-on-a-stick/blob/HEAD/common/src/main/resources/assets/crafting_on_a_stick/lang/en_us.json).

### How translations work

There are two ways to add translations:

#### Option 1 - Template (recommended)

```json
{
  "item.crafting_on_a_stick.template": "%s on a Stick",

  "crafting_on_a_stick.key.open_curios": "Open Crafting on a Stick",
  ...
}
```

The `%s` placeholder is replaced with the workbench name automatically.
For example, the above would produce `"Crafting Table on a Stick"`.

#### Option 2 - Exact keys

```json
{
  "item.crafting_on_a_stick.crafting_table": "Crafting Table on a Stick",
  "item.crafting_on_a_stick.loom": "Loom on a Stick",
  "item.crafting_on_a_stick.grindstone": "Grindstone on a Stick",
  "item.crafting_on_a_stick.cartography_table": "Cartography Table on a Stick",
  "item.crafting_on_a_stick.stonecutter": "Stonecutter on a Stick",
  "item.crafting_on_a_stick.smithing_table": "Smithing Table on a Stick",
  "item.crafting_on_a_stick.anvil": "Anvil on a Stick",
  "item.crafting_on_a_stick.chipped_anvil": "Chipped Anvil on a Stick",
  "item.crafting_on_a_stick.damaged_anvil": "Damaged Anvil on a Stick",
  
  "crafting_on_a_stick.key.open_curios": "Open Crafting on a Stick",
  ...
}
```

Use this when your language requires a structure that doesn't fit the `%s` template (e.g. different prefix/suffix per item).