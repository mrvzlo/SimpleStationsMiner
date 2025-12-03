package com.ave.simplestationsminer.datagen;

import java.util.concurrent.CompletableFuture;

import com.ave.simplestationsminer.SimpleStationsMiner;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleStationsMiner.DRILL_ITEM.get())
                .pattern("LIL")
                .pattern("RIR")
                .pattern("IBI")
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('L', Items.LAPIS_LAZULI)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleStationsMiner.MINER_BLOCK.get())
                .pattern("DRD")
                .pattern("RCR")
                .pattern("HRH")
                .define('D', Items.DEEPSLATE_BRICKS)
                .define('H', Items.HOPPER)
                .define('C', Items.MINECART)
                .define('R', Items.POWERED_RAIL)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleStationsMiner.PORTAL.get())
                .pattern("OOO")
                .pattern("OFO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('F', Items.FIRE_CHARGE)
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleStationsMiner.DRILL_ITEM_2.get())
                .pattern("OOO")
                .pattern("OFO")
                .pattern("OOO")
                .define('O', Items.DIAMOND)
                .define('F', SimpleStationsMiner.DRILL_ITEM.get())
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleStationsMiner.DRILL_ITEM_3.get())
                .pattern(" O ")
                .pattern("OFO")
                .pattern(" O ")
                .define('O', Items.NETHERITE_INGOT)
                .define('F', SimpleStationsMiner.DRILL_ITEM_2.get())
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);
    }
}