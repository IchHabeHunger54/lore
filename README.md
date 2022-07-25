# Lore Mod

Pages of a lost diary now appear in loot chests. Players can find those pages and put them together into a book. Note: This mod does not include a story by default, it needs writing and setup by the modpack developer.

Get the mod here: [https://www.curseforge.com/minecraft/mc-mods/lore-books](https://www.curseforge.com/minecraft/mc-mods/lore-books "https://www.curseforge.com/minecraft/mc-mods/lore-books")

Join the Discord for help: [https://discord.gg/GM6XSqM](https://discord.gg/GM6XSqM "https://discord.gg/GM6XSqM")

# How To Use

This section is meant for pack developers or server managers, it has no use for you if you are a regular player.

1. Make a datapack and a resource pack (`data` and `assets` folders, plus `pack.mcmeta` files). Make sure they are loaded. It is recommended to use a mod like [Open Loader](https://www.curseforge.com/minecraft/mc-mods/open-loader "https://www.curseforge.com/minecraft/mc-mods/open-loader") to globally load the datapack.

2. In your resource pack, make a folder named `lore`. In the `lore` folder, make another folder called `lang`. And in the `lang` folder, make a file called `en_us.json`.

3. In your datapack, make a folder named `lore`. In the `lore` folder, make another folder named `lore_pages`.

4. Now onto the pages themselves. Every page needs a JSON file with two properties: `book` and `number`. The JSON filename needs to be all lowercase, for example `my_book_15.json`.
    - `book` is the name of the book the page belongs to. All pages that have the same book name will be grouped into the same book. This way, it is possible to make multiple books, e.g. by naming them `book1` and `book2`.
    - `number` is the page number of the page. Due to the way this mod works internally, and to allow for future modifications to the page-specific structure, there needs to be a file for each page, rather than each book. Take caution to not miss any page numbers in the middle, as the mod does not check for that, and unexpected behavior may occur.
    - An example of a page JSON looks like this:

```json
{
  "book": "my_book",
  "number": 15
}
```

5. Once you have created your pages, switch to the previously created language file (`en_us.json`) and open it. Add the following to the file:

```json
{
  
}
```

6. Then, for each book you created, add the following line, where `<book>` is the name of the book, and `<translation>` is the desired name for your book:

```json
    "item.lore.lore_book.name.<book>": "<translation>"
```

The entire file would now look something like this:

```json
{
  "item.lore.lore_book.name.my_book": "My Book"
}
```

7. And finally, for each page you created, add the following line, where `<book>` is the book this page belongs to, `<number>` is the page number, and `<translation>` is the desired translation for your page (note that you need to make sure the translations fit onto the page yourself, the mod does not truncate text):

```json
    "item.lore.lore_page.<book>.<number>.text": "<translation>"
```

The entire file would now look something like this:

```json
{
  "item.lore.lore_book.name.my_book": "My Book",
  "item.lore.lore_page.my_book.15.text": "Hello World!"
}
```

8. (optional) If you want to support multiple languages, just make another lang file (e.g. `de_de.json` for German), copy over the contents of `en_us.json`, and edit the translations. `en_us.json` will always be used as a fallback.

9. Make sure all the pages correctly show up. The easiest way to do this is by checking the creative tab to see if all pages are there. You can also fly around in spectator mode, locate a few structures and check if your pages generate - they should generate in 20% of chests.

10. Now, you will want to get rid of the one test entry. You can easily do this by creating a file called `test.json` in the `lore_pages` folder and add the following content to it: `{}` Yeah, just those two characters.

11. Once you made sure everything works, you're done!
