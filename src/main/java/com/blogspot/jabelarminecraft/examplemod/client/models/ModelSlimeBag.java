package com.blogspot.jabelarminecraft.examplemod.client.models;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public final class ModelSlimeBag implements IModel
{
    public static final ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation(MainMod.MODID, "slime_bag"), "inventory");

    // minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    public static final IModel MODEL = new ModelSlimeBag();

    @Nullable
    private final ResourceLocation emptyLocation = new ResourceLocation(MainMod.MODID, "slime_bag_empty");
    @Nullable
    private final ResourceLocation filledLocation = new ResourceLocation(MainMod.MODID, "slime_bag");
    @Nullable
    private final Fluid fluid;

    public ModelSlimeBag()
    {
    	this(null);
    }
    
    public ModelSlimeBag(Fluid parFluid)
    {
    	fluid = parFluid;
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        if (emptyLocation != null)
            builder.add(emptyLocation);
        if (filledLocation != null)
            builder.add(filledLocation);

        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {

        ImmutableMap<TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        TextureAtlasSprite fluidSprite = null;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        if(fluid != null) {
            fluidSprite = bakedTextureGetter.apply(fluid.getStill());
        }

        if (emptyLocation != null)
        {
            // build base (insidest)
            IBakedModel model = (new ItemLayerModel(ImmutableList.of(emptyLocation))).bake(state, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
        }
        if (filledLocation != null && fluidSprite != null)
        {
            TextureAtlasSprite liquid = bakedTextureGetter.apply(filledLocation);
            // build liquid layer (inside)
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, fluid.getColor()));
            builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, fluid.getColor()));
        }


        return new Baked(this, builder.build(), fluidSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap());
    }

    /**
     * Sets the liquid in the model.
     * fluid - Name of the fluid in the FluidRegistry
     * If the fluid can't be found, water is used
     */
    @Override
    public ModelSlimeBag process(ImmutableMap<String, String> customData)
    {
        String fluidName = customData.get("fluid");
        Fluid fluid = FluidRegistry.getFluid(fluidName);

        if (fluid == null) fluid = this.fluid;

        // create new model with correct liquid
        return new ModelSlimeBag(fluid);
    }

    /**
     * Allows to use different textures for the model.
     * There are 3 layers:
     * base - The empty bucket/container
     * fluid - A texture representing the liquid portion. Non-transparent = liquid
     * cover - An overlay that's put over the liquid (optional)
     * <p/>
     * If no liquid is given a hardcoded variant for the bucket is used.
     */
    @Override
    public ModelSlimeBag retexture(ImmutableMap<String, String> textures)
    {
    	// don't allow retexturing

        return new ModelSlimeBag(fluid);
    }

    public enum CustomModelLoader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation)
        {
            return modelLocation.getResourceDomain().equals(MainMod.MODID) && modelLocation.getResourcePath().contains("slime_bag");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation)
        {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }
    }

    private static final class BakedOverrideHandler extends ItemOverrideList
    {
        public static final BakedOverrideHandler INSTANCE = new BakedOverrideHandler();
        private BakedOverrideHandler()
        {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            FluidStack fluidStack = FluidUtil.getFluidContained(stack);

            // not a fluid item apparently
            if (fluidStack == null)
            {
                // empty bucket
                return originalModel;
            }

            Baked model = (Baked)originalModel;

            Fluid fluid = fluidStack.getFluid();
            String name = fluid.getName();

            if (!model.cache.containsKey(name))
            {
                IModel parent = model.parent.process(ImmutableMap.of("fluid", name));
                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.format, textureGetter);
                model.cache.put(name, bakedModel);
                return bakedModel;
            }

            return model.cache.get(name);
        }
    }

    // the filled bucket is based on the empty bucket
    private static final class Baked implements IBakedModel
    {

        private final ModelSlimeBag parent;
        // FIXME: guava cache?
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;

        public Baked(ModelSlimeBag parent,
                              ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
                              Map<String, IBakedModel> cache)
        {
            this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return BakedOverrideHandler.INSTANCE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
        {
            return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
        {
            if(side == null) return quads;
            return ImmutableList.of();
        }

        @Override
		public boolean isAmbientOcclusion() { return true;  }
        @Override
		public boolean isGui3d() { return false; }
        @Override
		public boolean isBuiltInRenderer() { return false; }
        @Override
		public TextureAtlasSprite getParticleTexture() { return particle; }
    }
}