package com.mmodding.better_copper.copper_capabilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mmodding.better_copper.BetterCopper;
import com.mmodding.mmodding_lib.library.network.support.NetworkSupport;
import com.mmodding.mmodding_lib.library.utils.ShouldNotUse;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class CopperCapabilityDisplay implements NetworkSupport {

	private final Text title;
	private final Text description;
	private final ItemStack icon;
	private final @Nullable Identifier background;
	private final AdvancementFrame frame;

	private float x;
	private float y;

	public CopperCapabilityDisplay(Text title, Text description, ItemStack icon, @Nullable Identifier background, AdvancementFrame frame) {
		this.title = title;
		this.description = description;
		this.icon = icon;
		this.background = background;
		this.frame = frame;
	}

	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Text getTitle() {
		return this.title;
	}

	public Text getDescription() {
		return this.description;
	}

	public ItemStack getIcon() {
		return this.icon;
	}

	@Nullable
	public Identifier getBackground() {
		return this.background;
	}

	public AdvancementFrame getFrame() {
		return this.frame;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public static CopperCapabilityDisplay fromJson(JsonObject obj) {
		Text title = Text.Serializer.fromJson(obj.get("title"));
		Text description = Text.Serializer.fromJson(obj.get("description"));
		if (title != null && description != null) {
			ItemStack icon = AdvancementDisplay.iconFromJson(JsonHelper.getObject(obj, "icon"));
			Identifier background = obj.has("background") ? new Identifier(JsonHelper.getString(obj, "background")) : null;
			AdvancementFrame advancementFrame = obj.has("frame") ? AdvancementFrame.forName(JsonHelper.getString(obj, "frame")) : AdvancementFrame.TASK;
			return new CopperCapabilityDisplay(title, description, icon, background, advancementFrame);
		} else {
			throw new JsonSyntaxException("Both title and description must be set");
		}
	}

	public JsonElement toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("title", Text.Serializer.toJsonTree(this.title));
		jsonObject.add("description", Text.Serializer.toJsonTree(this.description));
		jsonObject.add("icon", iconToJson());
		jsonObject.addProperty("frame", this.frame.getId());
		if (this.background != null) {
			jsonObject.addProperty("background", this.background.toString());
		}
		return jsonObject;
	}

	public final JsonObject iconToJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("item", Registry.ITEM.getId(this.icon.getItem()).toString());
		if (this.icon.hasNbt()) {
			assert this.icon.getNbt() != null;
			jsonObject.addProperty("nbt", this.icon.getNbt().toString());
		}
		return jsonObject;
	}

	@ShouldNotUse(useInstead = "NetworkSupport#readComplete")
	public static CopperCapabilityDisplay read(PacketByteBuf buf) {
		Text title = buf.readText();
		Text description = buf.readText();
		ItemStack icon = buf.readItemStack();
		AdvancementFrame frame = buf.readEnumConstant(AdvancementFrame.class);
		CopperCapabilityDisplay display = new CopperCapabilityDisplay(title, description, icon, buf.readNullable(PacketByteBuf::readIdentifier), frame);
		display.setPos(buf.readFloat(), buf.readFloat());
		return display;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeText(this.title);
		buf.writeText(this.description);
		buf.writeItemStack(this.icon);
		buf.writeEnumConstant(this.frame);
		buf.writeNullable(this.background, PacketByteBuf::writeIdentifier);
		buf.writeFloat(this.x);
		buf.writeFloat(this.y);
	}

	static {
		NetworkSupport.register(BetterCopper.createId("copper_capability_display"), CopperCapabilityDisplay.class, CopperCapabilityDisplay::read);
	}
}
