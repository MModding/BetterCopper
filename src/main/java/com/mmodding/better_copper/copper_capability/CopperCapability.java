package com.mmodding.better_copper.copper_capability;

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

import java.util.function.Consumer;

public class CopperCapability {
	private final Identifier id;
	private final Text text;
	private final CopperCapability.Type capabilityType;
	@Nullable
	private final CopperCapabilityDisplay display;
	private final int price;
	private final int usePower;

	public CopperCapability(Identifier id, CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int price, int usePower) {
		this.id = id;
		this.capabilityType = capabilityType;
		this.display = display;
		this.price = price;
		this.usePower = usePower;

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
		return new CopperCapability.Task(this.capabilityType, this.display, this.price, this.usePower);
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

	public int getPrice() {
		return this.price;
	}

	public int getUsePower() {
		return this.usePower;
	}

	public String toString() {
		return "SimpleCopperCapability{id="
			+ this.getId()
			+ ", capabilityType="
			+ this.capabilityType
			+ ", display="
			+ this.display
			+ ", price="
			+ this.price
			+ ", usePower="
			+ this.usePower
			+ "}";
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
		ARMORS,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS,
		TOOLS,
		SWORD,
		PICKAXE,
		AXE,
		HOE;
	}

	public static class Task {
		private CopperCapability.Type capabilityType;
		@Nullable
		private CopperCapabilityDisplay display;
		private int price;
		private int usePower;

		Task(CopperCapability.Type capabilityType, @Nullable CopperCapabilityDisplay display, int price, int usePower) {
			this.capabilityType = capabilityType;
			this.display = display;
			this.price = price;
			this.usePower = usePower;
		}

		private Task() {}

		public static CopperCapability.Task create() {
			return new CopperCapability.Task();
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

		public CopperCapability build(Identifier id) {
			return new CopperCapability(id, capabilityType, this.display, this.price, this.usePower);
		}

		public CopperCapability build(Consumer<CopperCapability> consumer, String id) {
			CopperCapability copperCapability = this.build(BetterCopper.createId(id));
			consumer.accept(copperCapability);
			return copperCapability;
		}

		public JsonObject toJson() {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("type", this.capabilityType.toString());
			if (this.display != null) {
				jsonObject.add("display", this.display.toJson());
			}
			jsonObject.addProperty("price", this.price);
			jsonObject.addProperty("use_power", this.usePower);
			return jsonObject;
		}

		public void toPacket(PacketByteBuf buf) {
			buf.writeString(this.capabilityType.toString());
			buf.writeNullable(this.display, (bufx, display) -> display.toPacket(bufx));
			buf.writeVarInt(this.price);
			buf.writeVarInt(this.usePower);
		}

		public String toString() {
			return "Task CopperCapability{capabilityType="
				+ this.capabilityType
				+ ", display="
				+ this.display
				+ ", price="
				+ this.price
				+ ", usePower"
				+ this.usePower
				+ "}";
		}

		public static CopperCapability.Task fromJson(JsonObject obj) {
			String capabilityType = JsonHelper.getString(obj, "type");
			CopperCapabilityDisplay copperCapabilityDisplay = obj.has("display") ? CopperCapabilityDisplay.fromJson(JsonHelper.getObject(obj, "display")) : null;
			int price = JsonHelper.getInt(obj, "price");
			int usePower = JsonHelper.getInt(obj, "use_power");
			return new CopperCapability.Task(CopperCapability.Type.valueOf(capabilityType), copperCapabilityDisplay, price, usePower);
		}

		public static CopperCapability.Task fromPacket(PacketByteBuf buf) {
			String capabilityType = buf.readString();
			CopperCapabilityDisplay copperCapabilityDisplay = buf.readNullable(CopperCapabilityDisplay::fromPacket);
			int price = buf.readInt();
			int usePower = buf.readInt();
			return new CopperCapability.Task(CopperCapability.Type.valueOf(capabilityType), copperCapabilityDisplay, price, usePower);
		}
	}
}
