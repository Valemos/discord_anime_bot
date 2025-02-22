package game.materials;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;

class MaterialsSetTest {

    MaterialsSet emptySet;

    @BeforeEach
    void setUp() {
        emptySet = new MaterialsSet();
    }

    @Nonnull
    private MaterialsSet getMaterialsSet(int gold) {
        return getMaterialsSet(gold, 0);
    }

    @Nonnull
    private MaterialsSet getMaterialsSet(int gold, int diamond) {
        MaterialsSet newSet = new MaterialsSet();
        newSet.setAmount(Material.GOLD, gold);
        newSet.setAmount(Material.DIAMOND, diamond);
        return newSet;
    }

    @Test
    void createMaterialsEmpty() {
        for (Material m : Material.values()){
            assertEquals(0, emptySet.getAmount(m));
        }
    }

    @Test
    void testCompareMaterialsGold() {
        MaterialsSet gold100 = getMaterialsSet(100);
        MaterialsSet gold1000 = getMaterialsSet(1000);

        assertFalse(emptySet.containsEnough(gold100));
        assertTrue(gold100.containsEnough(emptySet));

        assertFalse(gold100.containsEnough(gold1000));
        assertTrue(gold1000.containsEnough(gold100));
    }

    @Test
    void testMultipleMaterialsCompare() {
        MaterialsSet m1 = getMaterialsSet(100, 10);
        MaterialsSet m2 = getMaterialsSet(10, 100);
        MaterialsSet m3 = getMaterialsSet(1000, 100);

        assertFalse(m1.containsEnough(m2));
        assertFalse(m2.containsEnough(m1));

        assertTrue(m3.containsEnough(m1));
        assertTrue(m3.containsEnough(m2));
    }

    @Test
    void testCompareIfEqual() {
        MaterialsSet m1 = getMaterialsSet(100, 100);
        MaterialsSet m2 = getMaterialsSet(100, 100);

        assertTrue(m1.containsEnough(m2));
        assertTrue(m2.containsEnough(m1));
        assertTrue(m1.containsEnough(m1));
        assertTrue(m2.containsEnough(m2));
    }

    @Test
    void testEmptyMaterialsDescription() {
        assertEquals(MaterialsSet.noMaterialsDescription, emptySet.getDescriptionMultiline());
    }
}