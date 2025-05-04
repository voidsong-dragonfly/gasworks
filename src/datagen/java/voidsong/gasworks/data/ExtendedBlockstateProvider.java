/*
 * BluSunrize
 * Copyright (c) 2023
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package voidsong.gasworks.data;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.Properties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class is heavily inspired and cribbed from my work with Immersive Engineering's ExtendedBlockStateProvider
 * The code for the variable-texture blocks is cribbed directly from my work on IE
 */
public abstract class ExtendedBlockstateProvider extends BlockStateProvider {

	protected final ExistingFileHelper existingFileHelper;

	public ExtendedBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Gasworks.MOD_ID, exFileHelper);
		this.existingFileHelper = exFileHelper;
	}

	protected String getName(Block b) {
		return BuiltInRegistries.BLOCK.getKey(b).getPath();
	}

	public void simpleBlockAndItem(Block b, ModelFile model) {
		simpleBlockAndItem(b, new ConfiguredModel(model));
	}

	protected void simpleBlockAndItem(Block b, ConfiguredModel model) {
		simpleBlock(b, model);
		itemModel(b, model.model);
	}

	public void multiBlockAndItem(Block b, ModelFile... models) {
		simpleBlock(b, Stream.of(models).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new));
		itemModel(b, new ConfiguredModel(models[0]).model);
	}

	protected void cubeAll(Block b, ResourceLocation texture)
	{
		cubeAll(b, texture, null);
	}

	protected void cubeAll(Block b, ResourceLocation texture, @Nullable RenderType type) {
		final BlockModelBuilder model = models().cubeAll(getName(b), texture);
		setRenderType(type, model);
		simpleBlockAndItem(b, model);
	}

	protected void multiCubeAll(Block b, ResourceLocation... textures) {
		multiCubeAll(b, null, textures);
	}

	protected void multiCubeAll(Block b, @Nullable RenderType type, ResourceLocation... textures) {
		final BlockModelBuilder[] models = new BlockModelBuilder[textures.length];
		for(int i = 0; i < textures.length; i++) {
			models[i] = models().cubeAll(getName(b)+i, textures[i]);
			setRenderType(type, models[i]);
		}
		multiBlockAndItem(b, models);
	}

	protected void multiEightCubeAll(Block b, ResourceLocation texture) {
		ResourceLocation[] textures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++)
			textures[i] = texture.withSuffix(Integer.toString(i));
		multiCubeAll(b, textures);
	}

	protected void slab(SlabBlock b, ResourceLocation texture) {
		slab(b, texture, null);
	}

	protected void slab(SlabBlock b, ResourceLocation texture, @Nullable RenderType type) {
		slab(b, texture, texture, texture, type);
	}

	protected void slab(SlabBlock b, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
		slab(b, side, top, bottom, null);
	}

	protected void slab(SlabBlock b, ResourceLocation side, ResourceLocation top, ResourceLocation bottom, @Nullable RenderType type) {
		ModelBuilder<?> mainModel = models().slab(getName(b)+"_bottom", side, bottom, top);
		ModelBuilder<?> topModel = models().slabTop(getName(b)+"_top", side, bottom, top);
		ModelBuilder<?> doubleModel = models().cubeBottomTop(getName(b)+"_double", side, bottom, top);
		setRenderType(type, mainModel, topModel, doubleModel);
		slabBlock(b, mainModel, topModel, doubleModel);
		itemModel(b, mainModel);
	}

	protected void slabMultiEightAll(SlabBlock b, ResourceLocation texture) {
		ResourceLocation[] textures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++)
			textures[i] = texture.withSuffix(Integer.toString(i));
		slabMultiAll(b, textures);
	}

	protected void slabMultiAll(SlabBlock b, ResourceLocation... textures) {
		slabMultiAll(b, null, textures);
	}

	protected void slabMultiAll(SlabBlock b, @Nullable RenderType type, ResourceLocation... textures) {
		final ModelBuilder<?>[] mainModels = new ModelBuilder<?>[textures.length];
		final ModelBuilder<?>[] topModels = new ModelBuilder<?>[textures.length];
		final ModelBuilder<?>[] doubleModels = new ModelBuilder<?>[textures.length];
		for(int i = 0; i < textures.length; i++) {
			mainModels[i] = models().slab(getName(b)+i+"_bottom", textures[i], textures[i], textures[i]);
			topModels[i] = models().slabTop(getName(b)+i+"_top", textures[i], textures[i], textures[i]);
			doubleModels[i] = models().cubeAll(getName(b)+i+"_double", textures[i]);
			setRenderType(type, mainModels[i], topModels[i], doubleModels[i]);
		}

		slabBlock(b, mainModels, topModels, doubleModels);
		itemModel(b, mainModels[0]);
	}

	//Forge method does not allow random textures for slabs, instead creating a ConfiguredModel directly from an input file
	public void slabBlock(SlabBlock b, ModelFile[] bottom, ModelFile[] top, ModelFile[] doubleslab) {
		getVariantBuilder(b)
				.partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(Stream.of(bottom).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new))
				.partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(Stream.of(top).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new))
				.partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(Stream.of(doubleslab).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new));
	}

	protected void stairsFor(StairBlock b, ResourceLocation texture) {
		stairs(b, texture, texture, texture, null);
	}

	protected void stairs(StairBlock b, ResourceLocation side, ResourceLocation top, ResourceLocation bottom, @Nullable RenderType type) {
		String baseName = getName(b);
		ModelBuilder<?> stairs = models().stairs(baseName, side, bottom, top);
		ModelBuilder<?> stairsInner = models().stairsInner(baseName+"_inner", side, bottom, top);
		ModelBuilder<?> stairsOuter = models().stairsOuter(baseName+"_outer", side, bottom, top);
		setRenderType(type, stairs, stairsInner, stairsOuter);
		stairsBlock(b, stairs, stairsInner, stairsOuter);
		itemModel(b, stairs);
	}

	protected void stairsForMultiEightAll(StairBlock b, ResourceLocation texture) {
		ResourceLocation[] textures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++)
			textures[i] = texture.withSuffix(Integer.toString(i));
		stairsForMultiAll(b, textures);
	}

	protected void stairsForMultiAll(StairBlock b, ResourceLocation... textures) {
		stairsForMultiAll(b, null, textures);
	}

	protected void stairsForMultiAll(StairBlock b, @Nullable RenderType type, ResourceLocation... textures) {
		final ModelBuilder<?>[] stairs = new ModelBuilder<?>[textures.length];
		final ModelBuilder<?>[] stairsInner = new ModelBuilder<?>[textures.length];
		final ModelBuilder<?>[] stairsOuter = new ModelBuilder<?>[textures.length];
		for(int i = 0; i < textures.length; i++) {
			stairs[i] = models().stairs(getName(b)+i, textures[i], textures[i], textures[i]);
			stairsInner[i] = models().stairsInner(getName(b)+i+"_inner", textures[i], textures[i], textures[i]);
			stairsOuter[i] = models().stairsOuter(getName(b)+i+"_outer", textures[i], textures[i], textures[i]);
			setRenderType(type, stairs[i], stairsInner[i], stairsOuter[i]);
		}

		stairsBlock(b, stairs, stairsInner, stairsOuter);
		itemModel(b, stairs[0]);
	}

	//Forge method does not allow random textures for slabs, instead creating a ConfiguredModel directly from an input file
	public void stairsBlock(StairBlock block, ModelFile[] stairs, ModelFile[] stairsInner, ModelFile[] stairsOuter) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					Direction facing = state.getValue(StairBlock.FACING);
					Half half = state.getValue(StairBlock.HALF);
					StairsShape shape = state.getValue(StairBlock.SHAPE);
					int yRot = (int)facing.getClockWise().toYRot(); // Stairs model is rotated 90 degrees clockwise for some reason
					if(shape==StairsShape.INNER_LEFT||shape==StairsShape.OUTER_LEFT)
						yRot += 270; // Left facing stairs are rotated 90 degrees clockwise
					if(shape!=StairsShape.STRAIGHT&&half==Half.TOP)
						yRot += 90; // Top stairs are rotated 90 degrees clockwise
					yRot %= 360;
					boolean uvlock = yRot!=0||half==Half.TOP; // Don't set uvlock for states that have no rotation
					//We need multiple textures, so no builder
					ModelFile[] files = (shape==StairsShape.STRAIGHT?stairs: shape==StairsShape.INNER_LEFT||shape==StairsShape.INNER_RIGHT?stairsInner: stairsOuter);
					ConfiguredModel[] models = new ConfiguredModel[stairs.length];
					for(int i = 0; i < stairs.length; i++)
						models[i] = new ConfiguredModel(files[i], half==Half.BOTTOM?0: 180, yRot, uvlock);
					return models;
				}, StairBlock.WATERLOGGED);
	}

	protected void wall(WallBlock b, ResourceLocation bottomTexture, ResourceLocation sideTexture, ResourceLocation topTexture) {
		wallBlock(b,
				wallModelTopped(getName(b)+"_post", "wall_post_topped", bottomTexture, sideTexture, topTexture),
				wallModelTopped(getName(b)+"_side", "wall_side_topped", bottomTexture, sideTexture, topTexture),
				wallModelTopped(getName(b)+"_side_tall", "wall_side_tall_topped", bottomTexture, sideTexture, topTexture)
		);
		itemModel(b, wallModelToppedInventory(getName(b), bottomTexture, sideTexture, topTexture));
	}

	protected void wallMultiEight(WallBlock b, ResourceLocation bottomTexture, ResourceLocation sideTexture, ResourceLocation topTexture) {
		ResourceLocation[] bottomTextures = new ResourceLocation[8];
		ResourceLocation[] sideTextures = new ResourceLocation[8];
		ResourceLocation[] topTextures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++) {
			bottomTextures[i] = bottomTexture.withSuffix(Integer.toString(i));
			sideTextures[i] = sideTexture.withSuffix(Integer.toString(i));
			topTextures[i] = topTexture.withSuffix(Integer.toString(i));
		}
		wallMultiMany(b, bottomTextures, sideTextures, topTextures);
	}

	protected void wallMultiMany(WallBlock b, ResourceLocation[] bottomTextures, ResourceLocation[] sideTextures, ResourceLocation[] topTextures) {
		wallMultiMany(b, null, bottomTextures, sideTextures, topTextures);
	}

	protected void wallMultiMany(WallBlock b, @Nullable RenderType type, ResourceLocation[] bottomTextures, ResourceLocation[] sideTextures, ResourceLocation[] topTextures) {
		final ModelBuilder<?>[] wallPost = new ModelBuilder<?>[bottomTextures.length];
		final ModelBuilder<?>[] wallSide = new ModelBuilder<?>[bottomTextures.length];
		final ModelBuilder<?>[] wallSideTall = new ModelBuilder<?>[bottomTextures.length];
		for(int i = 0; i < bottomTextures.length; i++) {
			wallPost[i] = wallModelTopped(getName(b)+i+"_post", "wall_post_topped", bottomTextures[i], sideTextures[i], topTextures[i]);
			wallSide[i] = wallModelTopped(getName(b)+i+"_side", "wall_side_topped", bottomTextures[i], sideTextures[i], topTextures[i]);
			wallSideTall[i] = wallModelTopped(getName(b)+i+"_side_tall", "wall_side_tall_topped", bottomTextures[i], sideTextures[i], topTextures[i]);
			setRenderType(type, wallPost[i], wallSide[i], wallSideTall[i]);
		}

		wallBlock(b, wallPost, wallSide, wallSideTall);
		itemModel(b, wallModelToppedInventory(getName(b), bottomTextures[0], sideTextures[0], topTextures[0]));
	}

	//Forge method does not allow random textures for walls, instead creating ConfiguredModel directly from input file
	public void wallBlock(WallBlock block, ModelFile[] posts, ModelFile[] sides, ModelFile[] sidesTall) {
		for(int i = 0; i < posts.length; i++) {
			ModelFile side = sides[i];
			ModelFile sideTall = sidesTall[i];
			MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
					.part().modelFile(posts[i]).addModel()
					.condition(WallBlock.UP, true).end();
			WALL_PROPS.entrySet().stream()
					.filter(e -> e.getKey().getAxis().isHorizontal())
					.forEach(e -> {
						wallSidePart(builder, side, e, WallSide.LOW);
						wallSidePart(builder, sideTall, e, WallSide.TALL);
					});
		}
	}

	//This method is private in BlockStateProvider & we need access to it
	private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Entry<Direction, Property<WallSide>> entry, WallSide height) {
		builder.part()
				.modelFile(model)
				.rotationY((((int)entry.getKey().toYRot())+180)%360)
				.uvLock(true)
				.addModel()
				.condition(entry.getValue(), height);
	}

	protected void setRenderType(@Nullable RenderType type, ModelBuilder<?>... builders) {
		if(type!=null) {
			final String typeName;
			if(type==RenderType.solid())
				typeName = "solid";
			else if(type==RenderType.translucent())
				typeName = "translucent";
			else if(type==RenderType.cutout())
				typeName = "cutout";
			else if(type==RenderType.cutoutMipped())
				typeName = "cutout_mipped";
			else
				throw new RuntimeException("Unknown render type: "+type);

			for(final ModelBuilder<?> model : builders)
				model.renderType(typeName);
		}
	}

	protected ResourceLocation addModelsPrefix(ResourceLocation in) {
		return in.withPath("models/"+in.getPath());
	}

	protected void itemModel(Block block, ModelFile model) {
		itemModels().getBuilder(getName(block)).parent(model);
	}

	protected BlockModelBuilder wallModelTopped(String name, String type, ResourceLocation bottom, ResourceLocation side, ResourceLocation top) {
		return models().withExistingParent(name, Gasworks.rl("block/"+type))
				.texture("wall_bottom", bottom)
				.texture("wall_side", side)
				.texture("wall_top", top);
	}

	protected BlockModelBuilder wallModelToppedInventory(String name, ResourceLocation bottom, ResourceLocation side, ResourceLocation top) {
		return models().withExistingParent(name, Gasworks.rl("block/wall_inventory_topped"))
				.texture("wall_bottom", bottom)
				.texture("wall_side", side)
				.texture("wall_top", top);
	}

	protected int getAngle(Direction dir, int offset) {
		return (int)((dir.toYRot()+offset)%360);
	}

	protected void horizontalFacingBlock(Block block, ModelFile model) {
		horizontalFacingBlock(block, $ -> model, List.of());
	}

	protected void horizontalFacingBlock(Block block, ModelFile model, int offsetRotY) {
		rotatedBlock(block, $ -> model, Properties.FACING_HORIZONTAL, List.of(), 0, offsetRotY);
	}

	protected void horizontalFacingBlock(Block block, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps) {
		rotatedBlock(block, model, Properties.FACING_HORIZONTAL, additionalProps, 0, 180);
	}

	protected void allFacingBlock(Block block, ModelFile model) {
		allFacingBlock(block, $ -> model, List.of());
	}

	protected void allFacingBlock(Block block, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps) {
		rotatedBlock(block, model, Properties.FACING_ALL, additionalProps, 90, 0);
	}

	protected void rotatedBlock(Block block, ModelFile model, Property<Direction> facing, List<Property<?>> additionalProps, int offsetRotX, int offsetRotY) {
		rotatedBlock(block, $ -> model, facing, additionalProps, offsetRotX, offsetRotY);
	}

	protected void rotatedBlock(Block block, Function<PartialBlockstate, ModelFile> model, Property<Direction> facing, List<Property<?>> additionalProps, int offsetRotX, int offsetRotY) {
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(block);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ModelFile modelLoc = model.apply(state);
			for(Direction d : facing.getPossibleValues()) {
				int x;
				int y;
				switch(d) {
					case UP -> {
						x = 90;
						y = 0;
					}
					case DOWN -> {
						x = -90;
						y = 0;
					}
					default -> {
						y = getAngle(d, offsetRotY);
						x = 0;
					}
				}
				state.with(facing, d).setModels(new ConfiguredModel(modelLoc, x+offsetRotX, y, false));
			}
		});
	}

	public static <T extends Comparable<T>> void forEach(PartialBlockstate base, Property<T> prop, List<Property<?>> remaining, Consumer<PartialBlockstate> out) {
		for(T value : prop.getPossibleValues())
			forEachState(base, remaining, map -> {
				map = map.with(prop, value);
				out.accept(map);
			});
	}

	public static void forEachState(PartialBlockstate base, List<Property<?>> props, Consumer<PartialBlockstate> out) {
		if(!props.isEmpty()) {
			List<Property<?>> remaining = props.subList(1, props.size());
			Property<?> main = props.getFirst();
			forEach(base, main, remaining, out);
		}
		else
			out.accept(base);
	}
}
