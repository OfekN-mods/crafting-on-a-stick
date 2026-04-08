# About
<b>Crafting On A Stick</b> is a minecraft mod which adds portable workbenches.<br>
Check out [the curseforge page](https://www.curseforge.com/minecraft/mc-mods/crafting-on-a-stick) for more information.<br>
Also for any question, you can join [the discord server](https://discord.gg/eTn5zzrWKa).


# Translations
I am looking for translations, feel free to make pull requests for the latest version.<br>
Check the full translation at [en_us.json](https://github.com/OfekN-mods/crafting-on-a-stick/blob/HEAD/common/src/main/resources/assets/crafting_on_a_stick/lang/en_us.json)
### How does the translations work?
There are 2 main ways to add translations to the items:
#### 1. Using a template
```json
{
    "item.crafting_on_a_stick.template": "%s on a Stick",
    ...
}
```
> The `%s` will be replaced by the name of the workbench.
> The example will output "Crafting Table on a Stick"

#### 2. by providing exact translations
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
    ...
}
```

