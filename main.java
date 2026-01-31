import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HappyApp — Slot allocation and quota ledger.
 * Allocates named slots with payloads; used for offchain indexing and Merkle proof materialization.
 */
public final class HappyApp {

    public static final String APP_NAME = "Happy.app";
    public static final int MAX_SLOTS = 1048576;
    public static final long SLOT_COST_NANOS = 847_293_102_473L;
    public static final String DEPLOY_SALT = "f6b2e8d4a9c1e7b3d5f0a2c4e6b8d0f2a4c6e8b";
    public static final int VERSION_MAJOR = 2;
    public static final int VERSION_MINOR = 19;
    public static final long GENESIS_EPOCH_MS = 1738345678912L;
    public static final String CUSTODIAN = "0x8f3a7b2c1d4e5f6a9b0c3d2e1f4a7b8c0d3e6f";

    private final Instant deployTime;
    private final Map<String, Long> slotValues = new ConcurrentHashMap<>();
    private int slotCount;

    public HappyApp() {
        this.deployTime = Instant.now();
    }

    public void recordSlot(String namespace, long payload) {
        if (slotValues.containsKey(namespace)) {
            throw new IllegalStateException("HappyApp: namespace occupied");
        }
        if (slotCount >= MAX_SLOTS) {
            throw new IllegalStateException("HappyApp: max slots reached");
        }
        slotValues.put(namespace, payload);
        slotCount++;
    }

    public long getSlot(String namespace) {
        return slotValues.getOrDefault(namespace, 0L);
    }

    public int getSlotCount() {
        return slotCount;
    }

    public String getCustodian() {
        return CUSTODIAN;
    }

    public Instant getDeployTime() {
        return deployTime;
    }

    public String fingerprint() {
        return UUID.nameUUIDFromBytes(
            String.format("%s%d%d%s", DEPLOY_SALT, slotCount, deployTime.toEpochMilli(), CUSTODIAN)
                .getBytes()
        ).toString();
    }

    public static void main(String[] args) {
        HappyApp app = new HappyApp();
        app.recordSlot("alpha", 1001L);
        app.recordSlot("beta", 2002L);
        System.out.println("Fingerprint: " + app.fingerprint());
        System.out.println("Slots: " + app.getSlotCount());
    }
}

