<img alt="frozen_apocalypse_logo" src="https://github.com/Jaggwagg/frozen-apocalypse-mod/assets/33637354/0d136f19-88e3-4486-b11e-9f0fe5b043d7">

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Jaggwagg/frozen-apocalypse-mod/build.yml?style=flat-square&logo=github)
![Static Badge](https://img.shields.io/badge/loader-fabric-fdf2d5?style=flat-square&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAADQAAAA4CAMAAACFZCuiAAAAGFBMVEUAAAA4NCqAem2akn6uppS8spzGvKXb0LRXf0qqAAAAAXRSTlMAQObYZgAAAH5JREFUSMfV1kEKwCAMRFFta73%2FjTsBA0GixK4mf9XN2wxVLGVTReU0YiSgoyOYAL3oCBIjO0JDIUiM7ABhSIx0AMmDdZQFWWChpOBCWdBqAKmNtiOQIfl4kDeAIvcvIEc3mqEdwD14xGgFtyABmmEIJEAWujdpUqTw1%2BOQB32HlBchz2KP1gAAAABJRU5ErkJggg%3D%3D&link=https%3A%2F%2Ffabricmc.net%2F)
![Dynamic JSON Badge](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fapi.modrinth.com%2Fv2%2Fproject%2FlP4Oae7B&query=downloads&style=flat-square&logo=modrinth&label=modrinth&color=lightgreen)
![Static Badge](https://img.shields.io/badge/curseforge-download-darkorange?style=flat-square&logo=curseforge&link=https%3A%2F%2Fwww.curseforge.com%2Fminecraft%2Fmc-mods%2Ffrozen-apocalypse)

Frozen Apocalypse is an open source Minecraft mod designed to add a challenge to your Minecraft world. The sun is quickly moving away from your world, eventually freezing the surface and making it uninhabitable.
<h4><ins>Warning! This will destroy your world! Make backups!</ins></h4>

## Forge Users
Frozen Apocalypse is compatible with Sinytra Connector (Minecraft 1.20.1). For instructions, click ![here](https://github.com/Sinytra/Connector).

## Premise
Have you ever wanted to freeze your Minecraft world, turning it into a frozen wasteland. Is Minecraft too easy for you or do you just want a reason to make an awesome underground base? If so, this mod is for you!

Each day, your world will quickly turn into a frozen, apocalyptic wasteland as the sun moves further away!

## Apocalypse
<details>
<summary>Day by day effects - Spoilers ahead</summary>
Most mobs freeze at or above the freezing Y level. Certain mobs will be converted to frozen variants.
<ul>
<li><b>Day 0: </b>Nothing happens. Gives you time to prepare.</li>
<li><b>Day 1: </b>10% smaller sun. 150 freezing level. Grass converts to frosted grass.</li>
<li><b>Day 2: </b>20% smaller sun. 112 freezing level. World freezing effects increase speed.</li>
<li><b>Day 3: </b>30% smaller sun. 84 freezing level. World freezing effects increase speed. Mobs survive daylight. All biomes snow. Water turns to ice. Icicles form. Leaves converts to dead leaves.</li>
<li><b>Day 4: </b>40% smaller sun. 62 freezing level. World freezing effects increase speed.</li>
<li><b>Day 5: </b>50% smaller sun. 45 freezing level. World freezing effects increase speed. Freezing damage increases. Grass converts to dead grass. Ice converts to packed ice. Lava converts to obsidian</li>
<li><b>Day 6: </b>60% smaller sun. 30 freezing level. World freezing effects increase speed. Freezing damage increases. Mobs can spawn during the day. It will snow full snow blocks.</li>
<li><b>Day 7: </b>70% smaller sun. 20 freezing level. World freezing effects increase speed. Freezing damage increases. </li>
<li><b>Day 8: </b>80% smaller sun. Same freezing level. World freezing effects increase speed. Freezing damage increases. Grass converts to permafrost. Leaves decay.</li>
<li><b>Day 9: </b>90% smaller sun. Same freezing level. World freezing effects increase speed. Freezing damage increases. </li>
</ul>
</details>

### Thermal Armor
In order to protect yourself from freezing to death on the surface, you can craft variants of thermal armor. You must wear all pieces of thermal armor in order to not freeze. You can wear different types of thermal armor.

### Frost Resistance
Frost resistance is a status effect which prevents a player or mob from freezing to death. If a player dies and the apocalypse level is high enough, the player will get 2 minutes of frost resistance. This should give players enough time to get to a safe location.

## Wiki
The wiki has all the available information about the mod, including crafting recipes. Visit it [here](https://github.com/Jaggwagg/frozen-apocalypse/wiki/Home).

## Dependencies
* Gradle
* JDK 17 or greater

## Compiling and Running
```shell
git clone https://github.com/Jaggwagg/frozen-apocalypse.git
```

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

## Contributing
### Rules
- Pull requests should always be reviewed by at least one other member prior to being merged
  - Exceptions include very small pull requests that are not critical to functionality (function renaming, etc.)
- Mark unfinished pull requests with the `wip` label
- When you start work on something you should have a pull request opened that same day or at least as soon as possible so others can be aware of the changes you are making
- You as the person opening the pull request should assign a reviewer
- The project leader has the final say for all pull requests and mergers

### Merging
Once your pull request has been Approved it may be merged at your discretion. In most cases responsibility for merging is left to the person who opened the pull request, however for simple pull requests it is fine for anyone to merge.

If substantive changes are made after the pull request has been marked Approved you should ask for an additional round of review.

- Use `squash and merge` if all commits in the PR can be summarized succinctly by a single message
- Use `rebase and merge` if each commit in the PR has its own significance
- Avoid just `merge` as it will create an extraneous merge commit

## License
* Current - [GPL-3.0](https://choosealicense.com/licenses/gpl-3.0/)
