# Frozen Apocalypse Mod - Changelog

### <ins>v1.2.3 - Unreleased</ins>

##### Added
- Config option for blue ice (#31)
- Config option for determining what mobs are allowed to spawn during apocalypse (#19)

##### Fixed
- Icicles not having a proper tool (#30)
- Entities riding entities freezing due to position being inside a block (#24)
- Blocks being placed above flowing fluids (#21)

##### Changed
- Icicles no longer form near light sources (#27)
- Icicles require a silk touch pickaxe to collect (unless they fall)
- Icicles behave more like ice

### <ins>v1.2.2 - Released 2024-1-27</ins>

##### Added
- Added golden thermal armor
- Added iron, gold, and diamond thermal horse armor (#14)

##### Fixed
- Creative or spectating players unnecessarily getting frost resistance effect after death
- Cryoboomer explosion placing blocks where it shouldn't (replacing obsidian/bedrock)
- Dimensions not being checked for snow in biomes (#13)
- Frozen variants converted in non-affected dimensions (#15)

### <ins>v1.2.1 - Released 2024-1-15</ins>

##### Fixed
- Dimension ids (actually world ids) not being checked properly
- Icy cobwebs not breaking fast with swords or shears

### <ins>v1.2.0 - Released 2024-1-12</ins>

##### Added
- Added a 9th apocalypse level, sun is now 90% smaller, entities freeze very quickly
- Added 4 new mob variants: Cryoboomer (creeper), Frostbite (zombie), Iceweaver (spider), Shiverstare (enderman)
- These variants are created if the corresponding mob is above the freezing Y level
- Added 5 new blocks: frosted grass, dead grass, dead leaves, icicles, and icy cobwebs
- Each new block spawns during certain apocalypse levels
- Added config option for minimum light level to avoid freezing for each apocalypse level, further levels increase the minimum light level
- Added config option for freezing immune entities
- Added config option for determining affected dimensions

##### Changed
- Almost everything about the apocalypse levels, major rewrite
- Reverted back to Minecraft 1.20.1, as multiple other mods are still stuck at this version
- All blocks that produce light are now considered heat sources
- Snow only spawns when snowing, instead of all the time
- Dry biomes will now snow (deserts, badlands, etc.)
- Mobs can now spawn during the day after day 5, config option available
- Config file warns the user via logs and makes a backup when there are syntax errors, type mismatches, and missing/unknown fields
- Config file now can back up into different file names

##### Fixed
- Apocalypse now lags the world even less due to multiple optimizations and feature removal
- Sun size change packet being registered every tick and returning incorrect data type
- Fixed a bunch of leaves remaining after decaying on trees (still might be some though)

##### Removed
- Removed the ability to add custom light source check (contributed to significant performance issues)
- Removed heat source check delay as it doesn't improve performance
- Removed severely outdated Simplified Chinese translation
- Removed disabling the weather

### <ins>v1.1.4 - Released 2023-12-23</ins>

##### Changed
- It now snows after a certain point in all biomes (only biomes that allow rain)

##### Fixed
- Fixed animals not spawning when they should
- Other fixes

### <ins>v1.1.3 - Released 2023-12-23</ins>

##### Added
- Added mob burning, sun sizes, and sun can change size to config

##### Changed
- Renamed gamerule frozenApocalypseUpdateSpeed to frozenApocalypseWorldUpdateSpeed

##### Fixed
- Fixed time bugs after adding custom apocalypses
- Fixed other bugs


### <ins>v1.1.2 - Released 2023-12-22</ins>

##### Added
- Allowed full customizable apocalypse via the config file. You can now change different effects including weather toggles, snow, and other effects. You can also make your own apocalypse levels

##### Changed
- Mobs will no longer spawn in the freezing zone (except undead, and spawners)

##### Fixed
- Fixed mod blocks not working as heat blocks, and allowed heat block radius option
- Fixed sun resize issues
- Other bug fixes

##### Removed
- Removed max apocalypse level


### <ins>v1.1.1 - Released 2023-11-30</ins>

##### Added
- Added 3 new gamerules: frozenApocalypseMaxLevel, frozenApocalypseUpdateSpeed, and frozenApocalypseDeathProtectionDuration

##### Changed
- Updated apocalypse effects to include an 8th level, which updates the world twice as fast

##### Fixed
- Fixed some performance issues, the apocalypse should lag the world significantly less on server side
- Fixed other bugs


### <ins>v1.1.0 - Released 2023-11-27</ins>


##### Added
- Two new variants of thermal armor, diamond and netherite

##### Changed
- Updated apocalypse effects, now a 7 stage progression
- Made surface harsher, player and mobs take more damage during later stages
- Replaced powder snow with regular snow so mobs can spawn on surface
- Zombies no longer take damage from the frost, mobs no longer catch on fire after day 2
- Players and mobs can now be warm near certain blocks like torches and lava

##### Fixed
- Sun size now decreases appropriately and works on servers
- Numerous bugs

##### Removed
- Simplified Chinese is no longer supported due to the renaming of items and effects

### <ins>v1.0.1 - Released 2023-03-15</ins>

##### Added
- Simplified Chinese support (thanks @Kasualix!)

##### Changed
- Updated to 1.19.4
