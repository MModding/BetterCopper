package com.mmodding.better_copper.copper_capabilities;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopperCapabilityPositioner {

	private final CopperCapability copperCapability;
	private final @Nullable CopperCapabilityPositioner parent;
	private final @Nullable CopperCapabilityPositioner previousSibling;
	private final int childrenSize;
	private final List<CopperCapabilityPositioner> children = Lists.newArrayList();
	private CopperCapabilityPositioner optionalLast;
	private @Nullable CopperCapabilityPositioner substituteChild;
	private int depth;
	private float row;
	private float relativeRowInSiblings;
	private float change;
	private float shift;

	public CopperCapabilityPositioner(CopperCapability copperCapability, @Nullable CopperCapabilityPositioner parent, @Nullable CopperCapabilityPositioner previousSibling, int childrenSize, int depth) {
		if (copperCapability.getDisplay() == null) {
			throw new IllegalArgumentException("Can't position an invisible copper capability!");
		} else {
			this.copperCapability = copperCapability;
			this.parent = parent;
			this.previousSibling = previousSibling;
			this.childrenSize = childrenSize;
			this.optionalLast = this;
			this.depth = depth;
			this.row = -1.0F;
			CopperCapabilityPositioner copperCapabilityPositioner = null;

			for (CopperCapability copperCapability2 : copperCapability.getChildren()) {
				copperCapabilityPositioner = this.findChildrenRecursively(copperCapability2, copperCapabilityPositioner);
			}
		}
	}

	@Nullable
	private CopperCapabilityPositioner findChildrenRecursively(CopperCapability copperCapability, @Nullable CopperCapabilityPositioner lastChild) {
		if (copperCapability.getDisplay() != null) {
			lastChild = new CopperCapabilityPositioner(copperCapability, this, lastChild, this.children.size() + 1, this.depth + 1);
			this.children.add(lastChild);
		} else {
			for (CopperCapability copperCapability2 : copperCapability.getChildren()) {
				lastChild = this.findChildrenRecursively(copperCapability2, lastChild);
			}
		}

		return lastChild;
	}

	private void calculateRecursively() {
		if (this.children.isEmpty()) {
			if (this.previousSibling != null) {
				this.row = this.previousSibling.row + 1.0F;
			} else {
				this.row = 0.0F;
			}
		} else {
			CopperCapabilityPositioner copperCapabilityPositioner = null;

			for (CopperCapabilityPositioner copperCapabilityPositioner2 : this.children) {
				copperCapabilityPositioner2.calculateRecursively();
				copperCapabilityPositioner = copperCapabilityPositioner2.onFinishCalculation(copperCapabilityPositioner == null ? copperCapabilityPositioner2 : copperCapabilityPositioner);
			}

			this.onFinishChildrenCalculation();
			float f = (this.children.get(0).row + this.children.get(this.children.size() - 1).row) / 2.0F;
			if (this.previousSibling != null) {
				this.row = this.previousSibling.row + 1.0F;
				this.relativeRowInSiblings = this.row - f;
			} else {
				this.row = f;
			}
		}
	}

	private float findMinRowRecursively(float deltaRow, int depth, float minRow) {
		this.row += deltaRow;
		this.depth = depth;
		if (this.row < minRow) {
			minRow = this.row;
		}

		for (CopperCapabilityPositioner copperCapabilityPositioner : this.children) {
			minRow = copperCapabilityPositioner.findMinRowRecursively(deltaRow + this.relativeRowInSiblings, depth + 1, minRow);
		}

		return minRow;
	}

	private void increaseRowRecursively(float deltaRow) {
		this.row += deltaRow;

		for (CopperCapabilityPositioner copperCapabilityPositioner : this.children) {
			copperCapabilityPositioner.increaseRowRecursively(deltaRow);
		}
	}

	private void onFinishChildrenCalculation() {
		float f = 0.0F;
		float g = 0.0F;

		for (int i = this.children.size() - 1; i >= 0; --i) {
			CopperCapabilityPositioner copperCapabilityPositioner = this.children.get(i);
			copperCapabilityPositioner.row += f;
			copperCapabilityPositioner.relativeRowInSiblings += f;
			g += copperCapabilityPositioner.change;
			f += copperCapabilityPositioner.shift + g;
		}
	}

	@Nullable
	private CopperCapabilityPositioner getFirstChild() {
		if (this.substituteChild != null) {
			return this.substituteChild;
		} else {
			return !this.children.isEmpty() ? this.children.get(0) : null;
		}
	}

	@Nullable
	private CopperCapabilityPositioner getLastChild() {
		if (this.substituteChild != null) {
			return this.substituteChild;
		} else {
			return !this.children.isEmpty() ? this.children.get(this.children.size() - 1) : null;
		}
	}

	private CopperCapabilityPositioner onFinishCalculation(CopperCapabilityPositioner last) {
		if (this.previousSibling != null) {
			CopperCapabilityPositioner copperCapabilityPositioner = this;
			CopperCapabilityPositioner copperCapabilityPositioner2 = this;
			CopperCapabilityPositioner copperCapabilityPositioner3 = this.previousSibling;
			CopperCapabilityPositioner copperCapabilityPositioner4 = this.parent.children.get(0);
			float f = this.relativeRowInSiblings;
			float g = this.relativeRowInSiblings;
			float h = copperCapabilityPositioner3.relativeRowInSiblings;
			float i;

			for (i = copperCapabilityPositioner4.relativeRowInSiblings;
				 copperCapabilityPositioner3.getLastChild() != null && copperCapabilityPositioner.getFirstChild() != null;
				 g += copperCapabilityPositioner2.relativeRowInSiblings) {
				copperCapabilityPositioner3 = copperCapabilityPositioner3.getLastChild();
				copperCapabilityPositioner = copperCapabilityPositioner.getFirstChild();
				copperCapabilityPositioner4 = copperCapabilityPositioner4.getFirstChild();
				copperCapabilityPositioner2 = copperCapabilityPositioner2.getLastChild();
				copperCapabilityPositioner2.optionalLast = this;
				float j = copperCapabilityPositioner3.row + h - (copperCapabilityPositioner.row + f) + 1.0F;
				if (j > 0.0F) {
					copperCapabilityPositioner3.getAncestor(this, last).pushDown(this, j);
					f += j;
					g += j;
				}

				h += copperCapabilityPositioner3.relativeRowInSiblings;
				f += copperCapabilityPositioner.relativeRowInSiblings;
				i += copperCapabilityPositioner4.relativeRowInSiblings;
			}

			if (copperCapabilityPositioner3.getLastChild() != null && copperCapabilityPositioner2.getLastChild() == null) {
				copperCapabilityPositioner2.substituteChild = copperCapabilityPositioner3.getLastChild();
				copperCapabilityPositioner2.relativeRowInSiblings += h - g;
			} else {
				if (copperCapabilityPositioner.getFirstChild() != null && copperCapabilityPositioner4.getFirstChild() == null) {
					copperCapabilityPositioner4.substituteChild = copperCapabilityPositioner.getFirstChild();
					copperCapabilityPositioner4.relativeRowInSiblings += f - i;
				}

				last = this;
			}

		}
		return last;
	}

	private void pushDown(CopperCapabilityPositioner positioner, float extraRowDistance) {
		float f = (float) (positioner.childrenSize - this.childrenSize);
		if (f != 0.0F) {
			positioner.change -= extraRowDistance / f;
			this.change += extraRowDistance / f;
		}

		positioner.shift += extraRowDistance;
		positioner.row += extraRowDistance;
		positioner.relativeRowInSiblings += extraRowDistance;
	}

	private CopperCapabilityPositioner getAncestor(CopperCapabilityPositioner potentialAncestor, CopperCapabilityPositioner defaultAncestor) {
		return this.optionalLast != null && potentialAncestor.parent.children.contains(this.optionalLast) ? this.optionalLast : defaultAncestor;
	}

	private void apply() {
		if (this.copperCapability.getDisplay() != null) {
			this.copperCapability.getDisplay().setPos((float) this.depth, this.row);
		}

		if (!this.children.isEmpty()) {
			for (CopperCapabilityPositioner advancementPositioner : this.children) {
				advancementPositioner.apply();
			}
		}
	}

	public static void arrangeForTree(CopperCapability root) {
		if (root.getDisplay() == null) {
			throw new IllegalArgumentException("Can't position children of an invisible root!");
		} else {
			CopperCapabilityPositioner copperCapabilityPositioner = new CopperCapabilityPositioner(root, null, null, 1, 0);
			copperCapabilityPositioner.calculateRecursively();
			float f = copperCapabilityPositioner.findMinRowRecursively(0.0F, 0, copperCapabilityPositioner.row);
			if (f < 0.0F) {
				copperCapabilityPositioner.increaseRowRecursively(-f);
			}

			copperCapabilityPositioner.apply();
		}
	}
}
