package calebzhou.rdimc.celestech.command.impl;

import calebzhou.rdimc.celestech.command.BaseCommand;
import net.minecraft.server.level.ServerPlayer;

import static calebzhou.rdimc.celestech.utils.TextUtils.sendChatMessage;

public class StruCommand extends BaseCommand implements ArgCommand {

    public StruCommand(String command, int permissionLevel) {
        super(command, permissionLevel,false);
    }

    @Override
    public void onExecute(ServerPlayer player, String stru) {
        player.getLevel().structureManager()./*
        RegistryAccess.Frozen frozen = player.getServer().registryAccess();
        Structure structure = frozen.registryOrThrow(Registry.STRUCTURE_REGISTRY).get(new ResourceLocation(stru));
        ChunkPos chunkPos = player.chunkPosition();
        ServerLevel level = player.getLevel();
        ChunkAccess chunkAccess = level.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_REFERENCES);
        chunkAccess.
        List<PiecesContainer> piecesContainerList = new ArrayList<>();
        piecesContainerList.add(new NetherFortressPieces());
        chunkAccess.setStartForStructure(structure, new StructureStart(structure,chunkPos,0, new PiecesContainer(new StructurePiece())));*/

    }

}
