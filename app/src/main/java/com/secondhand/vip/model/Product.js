const mongoose = require("mongoose");

const ProductSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
    },
    price: {
      type: Number,
      required: true,
    },

    // 舊：單圖（向下相容）
    imageUrl: {
      type: String,
      default: "",
    },

    // ⭐ 新：多圖
    imageUrls: {
      type: [String],
      default: [],
    },

    description: {
      type: String,
      default: "",
    },
  },
  { timestamps: true }
);

module.exports = mongoose.model("products", ProductSchema);
