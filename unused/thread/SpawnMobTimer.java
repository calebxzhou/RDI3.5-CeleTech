package calebzhou.rdimc.celestech.model.thread;

import calebzhou.rdimc.celestech.RDICeleTech;
import calebzhou.rdimc.celestech.model.BorderedBox;
import calebzhou.rdimc.celestech.model.CoordLocation;
import calebzhou.rdimc.celestech.model.VirtualStructure;
import calebzhou.rdimc.celestech.utils.ServerUtils;

import java.util.Collection;
import java.util.List;
import java.util.TimerTask;

public class SpawnMobTimer extends TimerTask {

    @Override
    public void run() {
        try {
            RDICeleTech.LOGGER.info("启动定时刷怪");
            Collection<List<VirtualStructure>> values = VirtualStructure.STRUCTURE_MAP.values();
            values.forEach(list-> list.forEach(stru->{
                    RDICeleTech.LOGGER.info("正在"+stru.getPname()+"设定的虚拟结构内刷怪");
                VirtualStructure.Type type = stru.getType();
                String mobName="";
                switch (type){
                    case swamp_hut -> mobName="witch";
                    case mansion -> mobName="";
                    case monument -> mobName="";
                }
                BorderedBox range = stru.getRange();
                CoordLocation loca = range.randomLocation();
                ServerUtils.executeCommandOnServer(String.format("summon %s %d %d %d",mobName,loca.getPosiX(), loca.getPosiY(),loca.getPosiZ()));

            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
