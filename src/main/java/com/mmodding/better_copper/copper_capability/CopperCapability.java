package com.mmodding.better_copper.copper_capability;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mmodding.better_copper.BetterCopper;
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
	@Nullable
	private final CopperCapability parent;
	private final Identifier id;
	private final Text text;
	private final CopperCapability.Type capabilityType;
	@Nullable
	private final CopperCapabilityDisplay display;
	private final int level;
	private final int price;
	private final int usePower;
	private final Set<CopperCapability> children = Sets.newLinkedHashSet();

	public CopperCapability(@Nullable CopperCapability parent, Identifier id, CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int level, int price, int usePower) {
		this.parent = parent;
		this.id = id;
		this.capabilityType = capabilityType;
		this.display = display;
		this.level = level;
		this.price = price;
		this.usePower = usePower;
		if (parent != null) {
			parent.addChild(this);
		}

		if (display == null) {
			this.text = Text.literal(id.toString());
		} else {
			Text text = display.getTitle();
			Formatting formatting = display.getFrame().getTitleFormat();
			Text textDescription = Texts.setStyleIfAbsent(text.copy(), Style.EMPTY.withColor(formatting)).append("\n").append(display.getDescription());
			Text finalHiddenText = text.copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, textDescription)));
			this.text = Texts.bracketed(finalHiddenText).formatted(formatting);
		}
	}

	public CopperCapability.Task createTask() {
		return new CopperCapability.Task(this.parent == null ? null : this.parent.getId(), this.capabilityType, this.display, this.level, this.price, this.usePower);
	}

	@Nullable
	public CopperCapability getParent() {
		return this.parent;
	}

	public Identifier getId() {
		return this.id;
	}

	public CopperCapability.Type getCapabilityType() {
		return this.capabilityType;
	}

	@Nullable
	public CopperCapabilityDisplay getDisplay() {
		return this.display;
	}

	public int getLevel() {
		return this.level;
	}

	public int getPrice() {
		return this.price;
	}

	public int getUsePower() {
		return this.usePower;
	}

	public String toString() {
		return "SimpleCopperCapability{id="
			+ this.getId()
			+ ", parent="
			+ (this.parent == null ? "null" : this.parent.getId())
			+ ", capabilityType="
			+ this.capabilityType
			+ ", display="
			+ this.display
			+ ", level="
			+ this.level
			+ ", price="
			+ this.price
			+ ", usePower="
			+ this.usePower
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
			return this.id.equals(copperCapability.id);
		}
	}

	public int hashCode() {
		return this.id.hashCode();
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

	public static class Task {
		@Nullable
		private Identifier parentId;
		@Nullable
		private CopperCapability parentObj;
		private CopperCapability.Type capabilityType;
		@Nullable
		private CopperCapabilityDisplay display;
		private int level;
		private int price;
		private int usePower;

		Task(@Nullable Identifier parentId, CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int level, int price, int usePower) {
			this.parentId = parentId;
			this.capabilityType = capabilityType;
			this.display = display;
			this.level = level;
			this.price = price;
			this.usePower = usePower;
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
			return new CopperCapability(parentObj, id, capabilityType, this.display, this.level, this.price, this.usePower);
		}

		public CopperCapability build(Consumer<CopperCapability> consumer, String id) {
			CopperCapability copperCapability = this.build(BetterCopper.createId(id));
			consumer.accept(copperCapability);
			return copperCapability;
		}

		public JsonObject toJson() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("type", this.capabilityType.toString());

			if (this.parentObj != null) {
				jsonObject.addProperty("parent", this.parentObj.getId().toString());
			} else if (this.parentId != null) {
				jsonObject.addProperty("parent", this.parentId.toString());
			}

			if (this.display != null) {
				jsonObject.add("display", this.display.toJson());
			}

			jsonObject.addProperty("level", this.level);
			jsonObject.addProperty("price", this.price);
			jsonObject.addProperty("use_power", this.usePower);
			return jsonObject;
		}

		public void toPacket(PacketByteBuf buf) {
			buf.writeString(this.capabilityType.toString());
			buf.writeNullable(this.parentId, PacketByteBuf::writeIdentifier);
			buf.writeNullable(this.display, (bufx, display) -> display.toPacket(bufx));
			buf.writeVarInt(this.level);
			buf.writeVarInt(this.price);
			buf.writeVarInt(this.usePower);
		}

		public String toString() {
			return "Task CopperCapability{capabilityType="
				+ this.capabilityType
				+ ", parentId="
				+ this.parentId
				+ ", display="
				+ this.display
				+ ", level="
				+ this.level
				+ ", price="
				+ this.price
				+ ", usePower"
				+ this.usePower
				+ "}";
		}

		public static CopperCapability.Task fromJson(JsonObject obj) {
			String capabilityType = JsonHelper.getString(obj, "type");
			Identifier parent = obj.has("parent") ? new Identifier(JsonHelper.getString(obj, "parent")) : null;
			CopperCapabilityDisplay copperCapabilityDisplay = obj.has("display") ? CopperCapabilityDisplay.fromJson(JsonHelper.getObject(obj, "display")) : null;
			int level = JsonHelper.getInt(obj, "level");
			int price = JsonHelper.getInt(obj, "price");
			int usePower = JsonHelper.getInt(obj, "use_power");
			return new CopperCapability.Task(parent, CopperCapability.Type.valueOf(capabilityType), copperCapabilityDisplay, level, price, usePower);
		}

		public static CopperCapability.Task fromPacket(PacketByteBuf buf) {
			String capabilityType = buf.readString();
			Identifier parent = buf.readNullable(PacketByteBuf::readIdentifier);
			CopperCapabilityDisplay copperCapabilityDisplay = buf.readNullable(CopperCapabilityDisplay::fromPacket);
			int level = buf.readInt();
			int price = buf.readInt();
			int usePower = buf.readInt();
			return new CopperCapability.Task(parent, CopperCapability.Type.valueOf(capabilityType), copperCapabilityDisplay, level, price, usePower);
		}
	}
}
