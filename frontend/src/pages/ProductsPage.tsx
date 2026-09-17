import { useEffect, useState } from "react";

import Navbar from "../components/Navbar";
import { getAllProducts } from "../services/productService";
import type { Product } from "../types/product";

function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] =
    useState("ALL");

  useEffect(() => {
    const loadProducts = async () => {
      try {
        const data = await getAllProducts();
        setProducts(data);
      } catch (error) {
        console.error("PRODUCT ERROR:", error);
        setError("Failed to load products.");
      } finally {
        setLoading(false);
      }
    };

    loadProducts();
  }, []);

  const categories = [
    ...new Set(
      products.map((product) => product.category)
    ),
  ];

  const filteredProducts = products.filter(
    (product) => {
      const matchesSearch =
        product.name
          .toLowerCase()
          .includes(searchTerm.toLowerCase());

      const matchesCategory =
        selectedCategory === "ALL" ||
        product.category === selectedCategory;

      return matchesSearch && matchesCategory;
    }
  );

  return (
    <div>
      <Navbar />

      <main>
        <h1>Products</h1>

        <div>
          <label htmlFor="search">
            Search Products
          </label>

          <br />

          <input
            id="search"
            type="text"
            placeholder="Search by product name..."
            value={searchTerm}
            onChange={(event) =>
              setSearchTerm(event.target.value)
            }
          />
        </div>

        <br />

        <div>
          <label htmlFor="category">
            Category
          </label>

          <br />

          <select
            id="category"
            value={selectedCategory}
            onChange={(event) =>
              setSelectedCategory(event.target.value)
            }
          >
            <option value="ALL">
              All Categories
            </option>

            {categories.map((category) => (
              <option
                key={category}
                value={category}
              >
                {category}
              </option>
            ))}
          </select>
        </div>

        <br />

        {loading && (
          <p>Loading products...</p>
        )}

        {error && (
          <p>{error}</p>
        )}

        {!loading &&
          !error &&
          filteredProducts.length === 0 && (
            <p>No matching products found.</p>
          )}

        {!loading &&
          !error &&
          filteredProducts.map((product) => (
            <div key={product.id}>

              <h3>{product.name}</h3>

              <p>
                {product.description}
              </p>

              <p>
                <strong>Price:</strong>{" "}
                €{product.price}
              </p>

              <p>
                <strong>Stock:</strong>{" "}
                {product.stockQuantity}
              </p>

              <p>
                <strong>Category:</strong>{" "}
                {product.category}
              </p>

              <hr />

            </div>
          ))}
      </main>
    </div>
  );
}

export default ProductsPage;