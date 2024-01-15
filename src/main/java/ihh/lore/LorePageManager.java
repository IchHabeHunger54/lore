package ihh.lore;

import com.github.minecraftschurlimods.codeclib.CodecDataManager;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LorePageManager extends CodecDataManager<LorePageManager.LorePageData> {
    private static final Lazy<LorePageManager> INSTANCE = Lazy.concurrentOf(LorePageManager::new);
    private List<LorePageData> randomCache;

    private LorePageManager() {
        super(Lore.MOD_ID, "lore_pages", LorePageData.CODEC); //TODO
        subscribeAsSyncable(Lore.NETWORK_HANDLER);
    }

    public static LorePageManager instance() {
        return INSTANCE.get();
    }

    public static Tag toNbt(LorePageData data, Tag orElse) {
        return LorePageData.CODEC.encodeStart(NbtOps.INSTANCE, data).get().mapRight(DataResult.PartialResult::message).ifRight(Lore.LOGGER::warn).left().orElse(orElse);
    }

    public static LorePageData fromNbt(Tag tag, LorePageData orElse) {
        return LorePageData.CODEC.decode(NbtOps.INSTANCE, tag).map(Pair::getFirst).get().mapRight(DataResult.PartialResult::message).ifRight(Lore.LOGGER::warn).left().orElse(orElse);
    }

    public LorePageData getByBookAndNumber(String book, int number) {
        for (LorePageData data : values()) {
            if (data.book().equals(book) && data.number() == number) return data;
        }
        return null;
    }

    public int getMaxPagesForBook(String book) {
        return Objects.equals(book, "") ? 0 : (int) values().stream()
                .filter(e -> e.book().equals(book))
                .distinct()
                .count();
    }

    public LorePageData getRandomPage() {
        if (randomCache == null || randomCache.size() != values().size()) {
            randomCache = new ArrayList<>(values());
        }
        Collections.shuffle(randomCache);
        return randomCache.get(0);
    }

    public record LorePageData(String book, int number) implements Comparable<LorePageData> {
        public static final Codec<LorePageData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("book").forGetter(LorePageData::book),
                Codec.INT.fieldOf("number").forGetter(LorePageData::number)
        ).apply(inst, LorePageData::new));

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LorePageData that = (LorePageData) o;
            return book.equals(that.book) && number == that.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(book, number);
        }

        @Override
        public int compareTo(@NotNull LorePageData o) {
            if (equals(o)) return 0;
            int i = book.compareTo(o.book);
            return i != 0 ? i : Integer.compare(number(), o.number());
        }
    }
}
