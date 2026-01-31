import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HappyApp — Slot allocation and quota ledger.
 * Allocates named slots with payloads; used for offchain indexing and Merkle proof materialization.
