#!/usr/bin/env python3
"""
Create Android launcher icons with two-line text layout.
Line 1: "Samrat"
Line 2: "777"
Android adaptive icons: only center 66% guaranteed visible.
"""

from PIL import Image, ImageDraw, ImageFont
import os

# Density configurations
densities = {
    "mdpi": 108,
    "hdpi": 162,
    "xhdpi": 216,
    "xxhdpi": 324,
    "xxxhdpi": 432,
}

# Crown emoji or you can use a crown image
# For now, we'll create text-based icon with crown symbol
CROWN_SYMBOL = "👑"  # Unicode crown emoji

def create_launcher_icon_with_text(output_path, size):
    """Create launcher icon with two-line text layout."""

    # Create transparent canvas
    canvas = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(canvas)

    # Calculate font sizes based on canvas size
    # Using smaller percentages to ensure text fits within safe zone
    font_size_line1 = int(size * 0.15)  # "Samrat" - 15% of canvas (reduced from 20%)
    font_size_line2 = int(size * 0.22)  # "777" - 22% of canvas (reduced from 28%)

    try:
        # Try to use a system font (bold for better visibility)
        font_line1 = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", font_size_line1)
        font_line2 = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", font_size_line2)
    except:
        # Fallback to default font
        font_line1 = ImageFont.load_default()
        font_line2 = ImageFont.load_default()

    # Text content
    text_line1 = "Samrat"
    text_line2 = "777"

    # Get text bounding boxes
    bbox1 = draw.textbbox((0, 0), text_line1, font=font_line1)
    bbox2 = draw.textbbox((0, 0), text_line2, font=font_line2)

    text_width1 = bbox1[2] - bbox1[0]
    text_height1 = bbox1[3] - bbox1[1]
    text_width2 = bbox2[2] - bbox2[0]
    text_height2 = bbox2[3] - bbox2[1]

    # Calculate total height and vertical spacing
    total_text_height = text_height1 + text_height2
    spacing = int(size * 0.03)  # 3% spacing between lines (reduced from 5%)

    # Calculate vertical positions (centered within safe zone - 70% of canvas)
    safe_zone_height = int(size * 0.70)
    safe_zone_top = (size - safe_zone_height) // 2
    y_line1 = safe_zone_top + (safe_zone_height - total_text_height - spacing) // 2
    y_line2 = y_line1 + text_height1 + spacing

    # Calculate horizontal positions (centered)
    x_line1 = (size - text_width1) // 2
    x_line2 = (size - text_width2) // 2

    # Colors: Orange for "Samrat", Black for "777"
    color_line1 = (246, 76, 22, 255)  # Orange #F64C16 for "Samrat"
    color_line2 = (0, 0, 0, 255)  # Black for "777"

    # Draw text (no outline)
    draw.text((x_line1, y_line1), text_line1, font=font_line1, fill=color_line1)
    draw.text((x_line2, y_line2), text_line2, font=font_line2, fill=color_line2)

    # Save
    canvas.save(output_path, "PNG")
    print(f"✓ Created {output_path} ({size}x{size})")

# Create icons for each density
for density, size in densities.items():
    output_path = f"app/src/main/res/mipmap-{density}/ic_launcher_foreground.png"
    create_launcher_icon_with_text(output_path, size)

print("\n✅ All launcher icons created successfully!")
print("Two-line layout: 'Samrat' (Orange #F64C16) / '777' (Black)")
print("Clean design with no outline or shadow.")
