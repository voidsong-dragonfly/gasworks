package voidsong.gasworks.data;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel.Builder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.block.SillBlock;
import voidsong.gasworks.common.block.properties.AshType;
import voidsong.gasworks.common.block.properties.GSProperties;

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

	public ExtendedBlockstateProvider(PackOutput output, String mod, ExistingFileHelper exFileHelper) {
		super(output, mod, exFileHelper);
		this.existingFileHelper = exFileHelper;
	}

	/*
	 * Library functions and other functions not directly related to models & blockstates; none of these should be accessed outside this class
	 */

	protected static ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, "block/" + path);
	}

	protected static ResourceLocation rlMC(String path) {
		return ResourceLocation.parse("block/" + path);
	}

	protected String getName(Block b) {
		return BuiltInRegistries.BLOCK.getKey(b).getPath();
	}

	private int getAngle(Direction dir, int offset) {
		return (int)((dir.toYRot()+offset)%360);
	}

	private static <T extends Comparable<T>> void forEach(PartialBlockstate base, Property<T> prop, List<Property<?>> remaining, Consumer<PartialBlockstate> out) {
		for(T value : prop.getPossibleValues())
			forEachState(base, remaining, map -> {
				map = map.with(prop, value);
				out.accept(map);
			});
	}

	private static void forEachState(PartialBlockstate base, List<Property<?>> props, Consumer<PartialBlockstate> out) {
		if(props!=null&&!props.isEmpty()) {
			List<Property<?>> remaining = props.subList(1, props.size());
			Property<?> main = props.getFirst();
			forEach(base, main, remaining, out);
		}
		else
			out.accept(base);
	}

	private void setRenderType(@Nullable RenderType type, ModelBuilder<?>... builders) {
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

	/*
	 * Item models & code to apply a model that will be the one model of a block to said block
	 */

	private void itemModel(Block block, ModelFile model) {
		itemModels().getBuilder(getName(block)).parent(model);
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

	protected void partialBlockAndItem(Block b, ModelFile item, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps) {
		itemModel(b, item);
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(b);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ModelFile modelLoc = model.apply(state);
			state.setModels(new ConfiguredModel(modelLoc));
		});
	}

	protected void multiPartialBlockAndItem(Block b, ModelFile item, Function<PartialBlockstate, ModelFile[]> model, List<Property<?>> additionalProps) {
		itemModel(b, item);
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(b);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ModelFile[] modelLoc = model.apply(state);
			ConfiguredModel[] models = new ConfiguredModel[modelLoc.length];
			for(int i = 0; i < models.length; i++)
				models[i] = new ConfiguredModel(modelLoc[i]);
			state.setModels(models);
		});
	}

	/*
	 * Six-sided cubes & other simple singular models
	 */

	protected void cubeAll(Block b, ResourceLocation texture) {
		cubeAll(b, texture, null);
	}

	protected void cubeAll(Block b, ResourceLocation texture, @Nullable RenderType type) {
		final BlockModelBuilder model = models().cubeAll(getName(b), texture);
		setRenderType(type, model);
		simpleBlockAndItem(b, model);
	}

	protected void multiEightCubeAll(Block b, ResourceLocation texture) {
		ResourceLocation[] textures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++)
			textures[i] = texture.withSuffix(Integer.toString(i));
		multiCubeAll(b, textures);
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

	public void sillMultiEight(SillBlock block, ResourceLocation brick, String stone) {
		sillMultiEight(block, brick, rlMC("polished_" + stone), stone);
	}

	public void sillMultiEight(SillBlock block, ResourceLocation brick, ResourceLocation full, String stone) {
		ResourceLocation sill = rl("stone/sills/" + stone);
		ResourceLocation single = rl("stone/bricks/single/" + stone);
		ModelFile[] topModels = new ModelFile[8];
		for(int i = 0; i < 8; i++) {
			topModels[i] = models().withExistingParent(getName(block)+"_top"+i, rl("sill"))
				.texture("brick", brick.withSuffix(Integer.toString(i)))
				.texture("down_overlay", brick.withSuffix(Integer.toString(i)))
				.texture("up_overlay", full)
				.texture("sill", sill);
		}
		ModelFile[] bottomModels = new ModelFile[8];
		for(int i = 0; i < 8; i++) {
			bottomModels[i] = models().withExistingParent(getName(block)+"_bottom"+i, rl("sill"))
				.texture("brick", brick.withSuffix(Integer.toString(i)))
				.texture("down_overlay", full)
				.texture("up_overlay", brick.withSuffix(Integer.toString(i)))
				.texture("sill", single);
		}
		multiPartialBlockAndItem(block, topModels[0], state -> (state.getSetStates().get(BlockStateProperties.HALF).equals(Half.TOP)) ? topModels : bottomModels, List.of(BlockStateProperties.HALF));
	}

	/*
	 * Slabs & slab models
	 */

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
	private void slabBlock(SlabBlock b, ModelFile[] bottom, ModelFile[] top, ModelFile[] doubleslab) {
		getVariantBuilder(b)
			.partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(Stream.of(bottom).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new))
			.partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(Stream.of(top).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new))
			.partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(Stream.of(doubleslab).map(ConfiguredModel::new).toArray(ConfiguredModel[]::new));
	}

	/*
	 * Stairs & stair models
	 */

	protected void stairs(StairBlock b, ResourceLocation texture) {
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

	protected void stairsMultiEightAll(StairBlock b, ResourceLocation texture) {
		ResourceLocation[] textures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++)
			textures[i] = texture.withSuffix(Integer.toString(i));
		stairsMultiAll(b, textures);
	}

	protected void stairsMultiAll(StairBlock b, ResourceLocation... textures) {
		stairsMultiAll(b, null, textures);
	}

	protected void stairsMultiAll(StairBlock b, @Nullable RenderType type, ResourceLocation... textures) {
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
	private void stairsBlock(StairBlock block, ModelFile[] stairs, ModelFile[] stairsInner, ModelFile[] stairsOuter) {
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

	/*
	 * Walls & wall models
	 */

	protected void wall(WallBlock b, ResourceLocation bottomTexture, ResourceLocation sideTexture, ResourceLocation topTexture) {
		wallBlock(b,
			wallModelTopped(getName(b)+"_post", "wall_post_topped", bottomTexture, sideTexture, topTexture),
			wallModelTopped(getName(b)+"_side", "wall_side_topped", bottomTexture, sideTexture, topTexture),
			wallModelTopped(getName(b)+"_side_tall", "wall_side_tall_topped", bottomTexture, sideTexture, topTexture)
		);
		itemModel(b, wallModelToppedInventory(getName(b), bottomTexture, sideTexture, topTexture));
	}

	protected void wallMultiEight(WallBlock b, ResourceLocation bottomTexture, ResourceLocation sideTexture, ResourceLocation topTexture, boolean variedTop) {
		ResourceLocation[] bottomTextures = new ResourceLocation[8];
		ResourceLocation[] sideTextures = new ResourceLocation[8];
		ResourceLocation[] topTextures = new ResourceLocation[8];
		for(int i = 0; i < 8; i++) {
			bottomTextures[i] = bottomTexture.withSuffix(Integer.toString(i));
			sideTextures[i] = sideTexture.withSuffix(Integer.toString(i));
			topTextures[i] = variedTop?topTexture.withSuffix(Integer.toString(i)):topTexture;
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
	private void wallBlock(WallBlock block, ModelFile[] posts, ModelFile[] sides, ModelFile[] sidesTall) {
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
		Builder<MultiPartBlockStateBuilder.PartBuilder> part = builder.part();
        for (int i = 0; i < posts.length; i++) {
            part = part.modelFile(posts[i]);
			if ((i + 1) < posts.length)
				part = part.nextModel();
        }
		part.addModel().condition(WallBlock.UP, true);
		WALL_PROPS.entrySet().stream()
			.filter(e -> e.getKey().getAxis().isHorizontal())
			.forEach(e -> {
				wallSidePart(builder, sides, e, WallSide.LOW);
				wallSidePart(builder, sidesTall, e, WallSide.TALL);
			});
	}

	//The equivalent method is private in BlockStateProvider & we need access to it
	private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile[] models, Entry<Direction, Property<WallSide>> entry, WallSide height) {
		Builder<MultiPartBlockStateBuilder.PartBuilder> part = builder.part();
		for (int i = 0; i < models.length; i++) {
			part = part.modelFile(models[i])
				.rotationY((((int)entry.getKey().toYRot())+180)%360)
				.uvLock(true);
			if ((i + 1) < models.length)
				part = part.nextModel();
		}
		part.addModel().condition(entry.getValue(), height);
	}

	private BlockModelBuilder wallModelTopped(String name, String type, ResourceLocation bottom, ResourceLocation side, ResourceLocation top) {
		return models().withExistingParent(name, Gasworks.rl("block/"+type))
			.texture("wall_bottom", bottom)
			.texture("wall_side", side)
			.texture("wall_top", top);
	}

	private BlockModelBuilder wallModelToppedInventory(String name, ResourceLocation bottom, ResourceLocation side, ResourceLocation top) {
		return models().withExistingParent(name, Gasworks.rl("block/wall_inventory_topped"))
			.texture("wall_bottom", bottom)
			.texture("wall_side", side)
			.texture("wall_top", top);
	}


	/*
	 * Axially rotatable blocks of some kind, incl. 3-axis & 2-axis (horizontal); as well as full-6-angle
	 */

	public void quoinMultiEight(HorizontalDirectionalBlock block, ResourceLocation brick, String stone) {
		ResourceLocation quoin = rl("stone/quoins/" + stone);
		ResourceLocation single = rl("stone/bricks/single/" + stone);
		ModelFile[] topModels = new ModelFile[8];
		for(int i = 0; i < 8; i++) {
			topModels[i] = models().withExistingParent(getName(block)+"_top"+i, rl("quoin"))
				.texture("brick", brick.withSuffix(Integer.toString(i)))
				.texture("quoin", quoin)
				.texture("quoin_reversed", quoin.withSuffix("_reversed"))
				.texture("single_brick", single)
				.texture("quoin_left", quoin.withSuffix("_left"))
				.texture("quoin_right", quoin.withSuffix("_right"));
		}
		ModelFile[] bottomModels = new ModelFile[8];
		for(int i = 0; i < 8; i++) {
			bottomModels[i] = models().withExistingParent(getName(block)+"_bottom"+i, rl("quoin_reversed"))
				.texture("brick", brick.withSuffix(Integer.toString(i)))
				.texture("quoin", quoin)
				.texture("quoin_reversed", quoin.withSuffix("_reversed"))
				.texture("single_brick", single)
				.texture("quoin_left", quoin.withSuffix("_left"))
				.texture("quoin_right", quoin.withSuffix("_right"));
		}
		rotatedBlock(block, state -> (state.getSetStates().get(BlockStateProperties.HALF).equals(Half.TOP)) ? topModels : bottomModels, HorizontalDirectionalBlock.FACING, List.of(BlockStateProperties.HALF), 0, 270);
		itemModel(block, topModels[0]);
	}

	public void logPileBlock(RotatedPillarBlock block, ResourceLocation log) {
		logPileBlock(block, log, ResourceLocation.fromNamespaceAndPath(log.getNamespace(), log.getPath() + "_top"));
	}

	public void logPileBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
		logPileBlock(block, models().withExistingParent(getName(block), rl("wood_pile"))
			.texture("side", side)
			.texture("top", end));
	}

	public void logPileBlock(RotatedPillarBlock block, ModelFile model) {
		getVariantBuilder(block)
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
			.modelForState().modelFile(model).addModel()
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
			.modelForState().modelFile(model).rotationX(90).addModel()
			.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
			.modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
		itemModel(block, model);
	}

	protected void horizontalFacing(Block block, ModelFile model) {
		horizontalFacing(block, $ -> new ModelFile[]{model}, List.of());
	}

	protected void horizontalFacing(Block block, ModelFile model, int offsetRotY) {
		rotatedBlock(block, $ -> new ModelFile[]{model}, GSProperties.FACING_HORIZONTAL, List.of(), 0, offsetRotY);
	}

	protected void horizontalFacing(Block block, Function<PartialBlockstate, ModelFile[]> models, List<Property<?>> additionalProps) {
		rotatedBlock(block, models, GSProperties.FACING_HORIZONTAL, additionalProps, 0, 180);
	}

	protected void allFacing(Block block, ModelFile model) {
		allFacing(block, $ -> new ModelFile[]{model}, List.of());
	}

	protected void allFacing(Block block, Function<PartialBlockstate, ModelFile[]> models, List<Property<?>> additionalProps) {
		rotatedBlock(block, models, GSProperties.FACING_ALL, additionalProps, 90, 0);
	}

	protected void rotatedBlock(Block block, ModelFile model, Property<Direction> facing, List<Property<?>> additionalProps, int offsetRotX, int offsetRotY) {
		rotatedBlock(block, $ -> new ModelFile[]{model}, facing, additionalProps, offsetRotX, offsetRotY);
	}

	protected void rotatedBlock(Block block, Function<PartialBlockstate, ModelFile[]> model, Property<Direction> facing, List<Property<?>> additionalProps, int offsetRotX, int offsetRotY) {
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(block);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ModelFile[] modelLoc = model.apply(state);
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
				ConfiguredModel[] models = new ConfiguredModel[modelLoc.length];
				for(int i = 0; i < models.length; i++)
					models[i] = new ConfiguredModel(modelLoc[i], x+offsetRotX, y, false);
				state.with(facing, d).setModels(models);
			}
		});
	}

	/*
	 * Randomized horizontal rotation blocks such as sand & dirt
	 */

	protected void ashPileCubeAll(Block b, List<Property<?>> additionalProps, ResourceLocation charcoal, ResourceLocation coke) {
		BlockModelBuilder charcoalModel = models().cubeAll(getName(b)+"_charcoal", charcoal);
		BlockModelBuilder cokeModel = models().cubeAll(getName(b)+"_coke", coke);
		ashPileCubeAll(b, additionalProps, charcoalModel, cokeModel);
	}

	protected void ashPileCubeAll(Block b, List<Property<?>> additionalProps, ModelFile charcoal, ModelFile coke) {
		horizontalRandomBlock(b, state -> state.getSetStates().get(PyrolyticAshBlock.ASH_TYPE).equals(AshType.CHARCOAL)?charcoal:coke, additionalProps);
	}

	protected void horizontalRandomCubeAllAndItem(Block b, List<Property<?>> additionalProps, ResourceLocation texture) {
		final BlockModelBuilder model = models().cubeAll(getName(b), texture);
		horizontalRandomBlockAndItem(b, model, additionalProps);
	}

	protected void horizontalRandomBlockAndItem(Block b, ModelFile model, List<Property<?>> additionalProps) {
		horizontalRandomBlock(b, model, additionalProps);
		itemModel(b, model);
	}

	protected void horizontalRandomBlock(Block b, ModelFile model, List<Property<?>> additionalProps) {
		horizontalRandomBlock(b, $ -> model, additionalProps);
	}

	protected void horizontalRandomBlockAndItem(Block b, ModelFile item, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps) {
		itemModel(b, item);
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(b);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ConfiguredModel[] models = new ConfiguredModel[4];
			ModelFile modelLoc = model.apply(state);
			for(int i = 0; i < 4; i++)
				models[i] = new ConfiguredModel(modelLoc, 0, i*90, false);
			state.setModels(models);
		});
	}

	protected void horizontalRandomBlock(Block b, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps) {
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(b);
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ConfiguredModel[] models = new ConfiguredModel[4];
			ModelFile modelLoc = model.apply(state);
			for(int i = 0; i < 4; i++)
				models[i] = new ConfiguredModel(modelLoc, 0, i*90, false);
			state.setModels(models);
		});
	}
}
