package com.mmodding.better_copper.copper_capabilities;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mmodding.better_copper.BetterCopper;
import com.mmodding.mmodding_lib.library.network.support.NetworkSupport;
import com.mmodding.mmodding_lib.library.utils.ShouldNotUse;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class CopperCapability {

	private final @Nullable CopperCapability parent;
	private final Identifier identifier;
	private final Text text;
	private final CopperCapability.Type capabilityType;
	private final @Nullable CopperCapabilityDisplay display;
	private final int maxLevel;
	private final int price;
	private final int requiredPower;
	private final Set<CopperCapability> children = Sets.newLinkedHashSet();

	public CopperCapability(@Nullable CopperCapability parent, Identifier identifier, CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int maxLevel, int price, int requiredPower) {
		this.parent = parent;
		this.identifier = identifier;
		this.capabilityType = capabilityType;
		this.display = display;
		this.maxLevel = maxLevel;
		this.price = price;
		this.requiredPower = requiredPower;
		if (parent != null) {
			parent.addChild(this);
		}

		if (display == null) {
			this.text = Text.literal(identifier.toString());
		} else {
			Text text = display.getTitle();
			Formatting formatting = display.getFrame().getTitleFormat();
			Text textDescription = Texts.setStyleIfAbsent(text.copy(), Style.EMPTY.withColor(formatting)).append("\n").append(display.getDescription());
			Text finalHiddenText = text.copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, textDescription)));
			this.text = Texts.bracketed(finalHiddenText).formatted(formatting);
		}
	}

	public CopperCapability.Task createTask() {
		return new CopperCapability.Task(this.parent == null ? null : this.parent.getIdentifier(), this.capabilityType, this.display, this.maxLevel, this.price, this.requiredPower);
	}

	@Nullable
	public CopperCapability getParent() {
		return this.parent;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	public CopperCapability.Type getCapabilityType() {
		return this.capabilityType;
	}

	@Nullable
	public CopperCapabilityDisplay getDisplay() {
		return this.display;
	}

	public int getMaxLevel() {
		return this.maxLevel;
	}

	public int getPrice() {
		return this.price;
	}

	public int getRequiredPower() {
		return this.requiredPower;
	}

	public String toString() {
		return "SimpleCopperCapability{id="
			+ this.getIdentifier()
			+ ", parent="
			+ (this.parent == null ? "null" : this.parent.getIdentifier())
			+ ", capabilityType="
			+ this.capabilityType
			+ ", display="
			+ this.display
			+ ", maxLevel="
			+ this.maxLevel
			+ ", price="
			+ this.price
			+ ", requiredPower="
			+ this.requiredPower
			+ "}";
	}

	public Iterable<CopperCapability> getChildren() {
		return this.children;
	}

	public void addChild(CopperCapability child) {
		this.children.add(child);
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof CopperCapability copperCapability)) {
			return false;
		} else {
			return this.identifier.equals(copperCapability.identifier);
		}
	}

	public int hashCode() {
		return this.identifier.hashCode();
	}

	public Text toHoverableText() {
		return this.text;
	}

	public enum Type {
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS,
		SWORD,
		PICKAXE,
		AXE,
		HOE;
	}

	public static class Task implements NetworkSupport {

		private @Nullable Identifier parentId;
		private @Nullable CopperCapability parentObj;
		private CopperCapability.Type capabilityType;
		private @Nullable CopperCapabilityDisplay display;
		private int maxLevel;
		private int price;
		private int requiredPower;

		Task(@Nullable Identifier parentId, CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int maxLevel, int price, int requiredPower) {
			this.parentId = parentId;
			this.capabilityType = capabilityType;
			this.display = display;
			this.maxLevel = maxLevel;
			this.price = price;
			this.requiredPower = requiredPower;
		}

		private Task() {}

		public static CopperCapability.Task create() {
			return new CopperCapability.Task();
		}

		public CopperCapability.Task parent(CopperCapability parent) {
			this.parentObj = parent;
			return this;
		}

		public CopperCapability.Task parent(Identifier parentId) {
			this.parentId = parentId;
			return this;
		}

		public CopperCapability.Task display(Text title, Text description, ItemStack icon, @Nullable Identifier background, AdvancementFrame frame) {
			return this.display(new CopperCapabilityDisplay(title, description, icon, background, frame));
		}

		public CopperCapability.Task display(Text title, Text description, ItemConvertible icon, @Nullable Identifier background, AdvancementFrame frame) {
			return this.display(new CopperCapabilityDisplay(title, description, new ItemStack(icon.asItem()), background, frame));
		}

		public CopperCapability.Task display(CopperCapabilityDisplay display) {
			this.display = display;
			return this;
		}

		public boolean findParent(Function<Identifier, CopperCapability> parentProvider) {
			if (this.parentId == null) {
				return true;
			} else {
				if (this.parentObj == null) {
					this.parentObj = parentProvider.apply(this.parentId);
				}
				return this.parentObj != null;
			}
		}

		public CopperCapability build(Identifier id) {
			return new CopperCapability(parentObj, id, capabilityType, this.display, this.maxLevel, this.price, this.requiredPower);
		}

		public CopperCapability build(Consumer<CopperCapability> consumer, String id) {
			CopperCapability copperCapability = this.build(BetterCopper.createId(id));
			consumer.accept(copperCapability);
			return copperCapability;
		}

		public static CopperCapability.Task fromJson(String capabilityType, JsonObject obj) {
			CopperCapability.Type type = CopperCapability.Type.valueOf(capabilityType.toUpperCase());
			Identifier parent = obj.has("parent") ? new Identifier(JsonHelper.getString(obj, "parent")) : null;
			CopperCapabilityDisplay copperCapabilityDisplay = obj.has("display") ? CopperCapabilityDisplay.fromJson(JsonHelper.getObject(obj, "display")) : null;
			int maxLevel = JsonHelper.getInt(obj, "max_level");
			int price = JsonHelper.getInt(obj, "price");
			int requiredPower = JsonHelper.getInt(obj, "required_power");
			return new CopperCapability.Task(parent, type, copperCapabilityDisplay, maxLevel, price, requiredPower);
		}

		public JsonObject toJson() {
			JsonObject jsonObject = new JsonObject();
			if (this.parentObj != null) {
				jsonObject.addProperty("parent", this.parentObj.getIdentifier().toString());
			} else if (this.parentId != null) {
				jsonObject.addProperty("parent", this.parentId.toString());
			}
			if (this.display != null) {
				jsonObject.add("display", this.display.toJson());
			}
			jsonObject.addProperty("max_level", this.maxLevel);
			jsonObject.addProperty("price", this.price);
			jsonObject.addProperty("required_power", this.requiredPower);
			return jsonObject;
		}

		@ShouldNotUse(useInstead = "NetworkSupport#readComplete")
		public static CopperCapability.Task read(PacketByteBuf buf) {
			Identifier parent = buf.readNullable(PacketByteBuf::readIdentifier);
			CopperCapability.Type type = buf.readEnumConstant(CopperCapability.Type.class);
			CopperCapabilityDisplay display = NetworkSupport.readCompleteAsNullable(buf);
			int maxLevel = buf.readVarInt();
			int price = buf.readVarInt();
			int requiredPower = buf.readVarInt();
			return new CopperCapability.Task(parent, type, display, maxLevel, price, requiredPower);
		}

		@Override
		public void write(PacketByteBuf buf) {
			buf.writeNullable(this.parentId, PacketByteBuf::writeIdentifier);
			buf.writeEnumConstant(this.capabilityType);
			NetworkSupport.writeCompleteAsNullable(this.display, buf);
			buf.writeVarInt(this.maxLevel);
			buf.writeVarInt(this.price);
			buf.writeVarInt(this.requiredPower);
		}

		public String toString() {
			return "Task CopperCapability{capabilityType="
				+ this.capabilityType
				+ ", parentId="
				+ this.parentId
				+ ", display="
				+ this.display
				+ ", maxLevel="
				+ this.maxLevel
				+ ", price="
				+ this.price
				+ ", requiredPower="
				+ this.requiredPower
				+ "}";
		}

		static {
			NetworkSupport.register(BetterCopper.createId("copper_capability_task"), CopperCapability.Task.class, Task::read);
		}
	}
}
