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
    # Using percentages to scale properly across densities
    font_size_line1 = int(size * 0.20)  # "Samrat" - 20% of canvas
    font_size_line2 = int(size * 0.28)  # "777" - 28% of canvas (bigger)

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
    spacing = int(size * 0.05)  # 5% spacing between lines

    # Calculate vertical positions (centered)
    y_line1 = (size - total_text_height - spacing) // 2
    y_line2 = y_line1 + text_height1 + spacing

    # Calculate horizontal positions (centered)
    x_line1 = (size - text_width1) // 2
    x_line2 = (size - text_width2) // 2

    # Gold/Yellow color for premium look
    text_color = (255, 215, 0, 255)  # Gold

    # Draw text with outline for better visibility
    outline_color = (0, 0, 0, 200)  # Semi-transparent black
    outline_width = max(1, size // 100)

    # Draw outline
    for adj_x in range(-outline_width, outline_width + 1):
        for adj_y in range(-outline_width, outline_width + 1):
            draw.text((x_line1 + adj_x, y_line1 + adj_y), text_line1, font=font_line1, fill=outline_color)
            draw.text((x_line2 + adj_x, y_line2 + adj_y), text_line2, font=font_line2, fill=outline_color)

    # Draw main text
    draw.text((x_line1, y_line1), text_line1, font=font_line1, fill=text_color)
    draw.text((x_line2, y_line2), text_line2, font=font_line2, fill=text_color)

    # Save
    canvas.save(output_path, "PNG")
    print(f"✓ Created {output_path} ({size}x{size})")

# Create icons for each density
for density, size in densities.items():
    output_path = f"app/src/main/res/mipmap-{density}/ic_launcher_foreground.png"
    create_launcher_icon_with_text(output_path, size)

print("\n✅ All launcher icons created successfully!")
print("Two-line layout: 'Samrat' / '777' with gold text and black outline.")
