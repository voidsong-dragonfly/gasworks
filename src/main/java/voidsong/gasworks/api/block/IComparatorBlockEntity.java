package voidsong.gasworks.api.block;

public interface IComparatorBlockEntity {
    default int getComparatorOutput() {
        return 0;
    }
}
