package calebzhou.rdi.core.server.module.tickinv;

public interface ITickDelayable {
    int getDelayTickListSize();
    void releaseDelayTickList();
}
