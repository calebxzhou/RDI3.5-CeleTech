scoreboard objectives add check dummy
scoreboard players add @a check 1
execute as @a[scores={check=1}] run setblock 0 61 0 minecraft:structure_block[mode=load]{metadata:"",mirror:"NONE",ignoreEntities:0b,powered:0b,seed:0L,author:"?",rotation:"NONE",posX:1,mode:"LOAD",posY:1,sizeX:5,posZ:1,integrity:1.0f,showair:0b,name:"ocean:raft",sizeY:6,sizeZ:8,showboundingbox:1b}
execute as @a[scores={check=1}] run setblock -1 61 0 minecraft:redstone_block
execute as @a[scores={check=1}] run setblock 0 61 0 air
execute as @a[scores={check=1}] run setblock -1 61 0 air
execute as @a[scores={check=1}] run tp @a 2.5 64.00 4.5
execute as @a[scores={check=1}] run scoreboard players add @a check