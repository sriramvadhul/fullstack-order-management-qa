import {
  useEffect,
  useState,
} from "react";

import Navbar from "../components/Navbar";

import {
  createProduct,
  deleteProduct,
  getAllProducts,
  updateProduct,
} from "../services/productService";

import {
  getAllAdminOrders,
  updateOrderStatus,
} from "../services/orderService";

import type {
  Product,
} from "../types/product";

import type {
  Order,
} from "../types/order";

const ORDER_STATUSES = [
  "PENDING",
  "CONFIRMED",
  "PROCESSING",
  "SHIPPED",
  "DELIVERED",
  "CANCELLED",
];

function AdminDashboardPage() {
  const [products, setProducts] =
    useState<Product[]>([]);

  const [orders, setOrders] =
    useState<Order[]>([]);

  const [name, setName] =
    useState("");

  const [
    description,
    setDescription,
  ] = useState("");

  const [price, setPrice] =
    useState("");

  const [
    stockQuantity,
    setStockQuantity,
  ] = useState("");

  const [category, setCategory] =
    useState("");

  const [
    editingProductId,
    setEditingProductId,
  ] = useState<number | null>(null);

  const [loadingProducts, setLoadingProducts] =
    useState(true);

  const [loadingOrders, setLoadingOrders] =
    useState(true);

  const [submitting, setSubmitting] =
    useState(false);

  const [
    deletingProductId,
    setDeletingProductId,
  ] = useState<number | null>(null);

  const [
    updatingOrderId,
    setUpdatingOrderId,
  ] = useState<number | null>(null);

  const [message, setMessage] =
    useState("");

  const [error, setError] =
    useState("");

  const loadProducts =
    async () => {

      try {
        const data =
          await getAllProducts();

        setProducts(data);

      } catch (error) {
        console.error(
          "LOAD PRODUCTS ERROR:",
          error
        );

        setError(
          "Failed to load products."
        );

      } finally {
        setLoadingProducts(false);
      }
    };

  const loadOrders =
    async () => {

      try {
        const data =
          await getAllAdminOrders();

        setOrders(data);

      } catch (error) {
        console.error(
          "LOAD ADMIN ORDERS ERROR:",
          error
        );

        setError(
          "Failed to load admin orders."
        );

      } finally {
        setLoadingOrders(false);
      }
    };

  useEffect(() => {
    loadProducts();
    loadOrders();
  }, []);

  const resetForm = () => {
    setName("");
    setDescription("");
    setPrice("");
    setStockQuantity("");
    setCategory("");
    setEditingProductId(null);
  };

  const handleSubmit =
    async (
      event:
        React.FormEvent<HTMLFormElement>
    ) => {

      event.preventDefault();

      setMessage("");
      setError("");
      setSubmitting(true);

      const productData = {
        name,
        description,
        price: Number(price),
        stockQuantity:
          Number(stockQuantity),
        category,
        active: true,
      };

      try {
        if (
          editingProductId === null
        ) {

          const createdProduct =
            await createProduct(
              productData
            );

          setMessage(
            `Product created successfully. Product ID: ${createdProduct.id}`
          );

        } else {

          await updateProduct(
            editingProductId,
            productData
          );

          setMessage(
            `Product ${editingProductId} updated successfully.`
          );
        }

        resetForm();

        await loadProducts();

      } catch (error) {
        console.error(
          "SAVE PRODUCT ERROR:",
          error
        );

        setError(
          "Failed to save product."
        );

      } finally {
        setSubmitting(false);
      }
    };

  const handleEdit = (
    product: Product
  ) => {

    setMessage("");
    setError("");

    setEditingProductId(
      product.id
    );

    setName(
      product.name
    );

    setDescription(
      product.description ?? ""
    );

    setPrice(
      String(product.price)
    );

    setStockQuantity(
      String(
        product.stockQuantity
      )
    );

    setCategory(
      product.category
    );

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  const handleCancelEdit =
    () => {

      resetForm();

      setMessage(
        "Edit cancelled."
      );

      setError("");
    };

  const handleDelete =
    async (
      product: Product
    ) => {

      const confirmed =
        window.confirm(
          `Delete "${product.name}"?`
        );

      if (!confirmed) {
        return;
      }

      setMessage("");
      setError("");

      setDeletingProductId(
        product.id
      );

      try {
        await deleteProduct(
          product.id
        );

        setProducts(
          (currentProducts) =>
            currentProducts.filter(
              (currentProduct) =>
                currentProduct.id !==
                product.id
            )
        );

        if (
          editingProductId ===
          product.id
        ) {
          resetForm();
        }

        setMessage(
          `Product ${product.id} deleted successfully.`
        );

      } catch (error) {
        console.error(
          "DELETE PRODUCT ERROR:",
          error
        );

        setError(
          "Failed to delete product."
        );

      } finally {
        setDeletingProductId(
          null
        );
      }
    };

  const handleOrderStatusChange =
    async (
      orderId: number,
      status: string
    ) => {

      setMessage("");
      setError("");

      setUpdatingOrderId(
        orderId
      );

      try {
        const updatedOrder =
          await updateOrderStatus(
            orderId,
            status
          );

        setOrders(
          (currentOrders) =>
            currentOrders.map(
              (order) =>
                order.orderId ===
                updatedOrder.orderId
                  ? updatedOrder
                  : order
            )
        );

        setMessage(
          `Order ${orderId} status updated to ${status}.`
        );

      } catch (error) {
        console.error(
          "UPDATE ORDER STATUS ERROR:",
          error
        );

        setError(
          `Failed to update order ${orderId}.`
        );

      } finally {
        setUpdatingOrderId(
          null
        );
      }
    };

  const formatDate = (
    date: string
  ) => {

    return new Date(
      date
    ).toLocaleString();
  };

  return (
    <div>
      <Navbar />

      <main>
        <h1>
          Admin Dashboard
        </h1>

        {message && (
          <p>
            {message}
          </p>
        )}

        {error && (
          <p>
            {error}
          </p>
        )}

        <section>
          <h2>
            {editingProductId ===
            null
              ? "Create Product"
              : `Edit Product #${editingProductId}`}
          </h2>

          <form
            onSubmit={
              handleSubmit
            }
          >
            <div>
              <label
                htmlFor="productName"
              >
                Product Name
              </label>

              <br />

              <input
                id="productName"
                type="text"
                value={name}
                required
                onChange={(
                  event
                ) =>
                  setName(
                    event.target.value
                  )
                }
              />
            </div>

            <br />

            <div>
              <label
                htmlFor="description"
              >
                Description
              </label>

              <br />

              <textarea
                id="description"
                value={description}
                onChange={(
                  event
                ) =>
                  setDescription(
                    event.target.value
                  )
                }
              />
            </div>

            <br />

            <div>
              <label
                htmlFor="price"
              >
                Price (€)
              </label>

              <br />

              <input
                id="price"
                type="number"
                min="0"
                step="0.01"
                value={price}
                required
                onChange={(
                  event
                ) =>
                  setPrice(
                    event.target.value
                  )
                }
              />
            </div>

            <br />

            <div>
              <label
                htmlFor="stock"
              >
                Stock Quantity
              </label>

              <br />

              <input
                id="stock"
                type="number"
                min="0"
                step="1"
                value={
                  stockQuantity
                }
                required
                onChange={(
                  event
                ) =>
                  setStockQuantity(
                    event.target.value
                  )
                }
              />
            </div>

            <br />

            <div>
              <label
                htmlFor="category"
              >
                Category
              </label>

              <br />

              <input
                id="category"
                type="text"
                value={category}
                required
                onChange={(
                  event
                ) =>
                  setCategory(
                    event.target.value
                  )
                }
              />
            </div>

            <br />

            <button
              type="submit"
              disabled={
                submitting
              }
            >
              {submitting
                ? "Saving..."
                : editingProductId ===
                    null
                  ? "Create Product"
                  : "Update Product"}
            </button>

            {editingProductId !==
              null && (
              <>
                {" "}

                <button
                  type="button"
                  disabled={
                    submitting
                  }
                  onClick={
                    handleCancelEdit
                  }
                >
                  Cancel Edit
                </button>
              </>
            )}
          </form>
        </section>

        <hr />

        <section>
          <h2>
            Product Management
          </h2>

          {loadingProducts && (
            <p>
              Loading products...
            </p>
          )}

          {!loadingProducts &&
            products.length ===
              0 && (
              <p>
                No products found.
              </p>
            )}

          {!loadingProducts &&
            products.length >
              0 && (
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Active</th>
                    <th>Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {products.map(
                    (
                      product
                    ) => (
                      <tr
                        key={
                          product.id
                        }
                      >
                        <td>
                          {product.id}
                        </td>

                        <td>
                          {product.name}
                        </td>

                        <td>
                          {product.category}
                        </td>

                        <td>
                          €
                          {Number(
                            product.price
                          ).toFixed(2)}
                        </td>

                        <td>
                          {
                            product.stockQuantity
                          }
                        </td>

                        <td>
                          {product.active
                            ? "Yes"
                            : "No"}
                        </td>

                        <td>
                          <button
                            type="button"
                            disabled={
                              deletingProductId ===
                              product.id
                            }
                            onClick={() =>
                              handleEdit(
                                product
                              )
                            }
                          >
                            Edit
                          </button>

                          {" "}

                          <button
                            type="button"
                            disabled={
                              deletingProductId ===
                              product.id
                            }
                            onClick={() =>
                              handleDelete(
                                product
                              )
                            }
                          >
                            {deletingProductId ===
                            product.id
                              ? "Deleting..."
                              : "Delete"}
                          </button>
                        </td>
                      </tr>
                    )
                  )}
                </tbody>
              </table>
            )}
        </section>

        <hr />

        <section>
          <h2>
            Order Management
          </h2>

          {loadingOrders && (
            <p>
              Loading orders...
            </p>
          )}

          {!loadingOrders &&
            orders.length ===
              0 && (
              <p>
                No orders found.
              </p>
            )}

          {!loadingOrders &&
            orders.map(
              (order) => (
                <div
                  key={
                    order.orderId
                  }
                >
                  <h3>
                    Order #
                    {order.orderId}
                  </h3>

                  <p>
                    <strong>
                      Created:
                    </strong>{" "}
                    {formatDate(
                      order.createdAt
                    )}
                  </p>

                  <p>
                    <strong>
                      Total:
                    </strong>{" "}
                    €
                    {Number(
                      order.totalAmount
                    ).toFixed(2)}
                  </p>

                  <div>
                    <label
                      htmlFor={
                        `status-${order.orderId}`
                      }
                    >
                      <strong>
                        Status:
                      </strong>
                    </label>

                    {" "}

                    <select
                      id={
                        `status-${order.orderId}`
                      }
                      value={
                        order.status
                      }
                      disabled={
                        updatingOrderId ===
                        order.orderId
                      }
                      onChange={(
                        event
                      ) =>
                        handleOrderStatusChange(
                          order.orderId,
                          event.target.value
                        )
                      }
                    >
                      {ORDER_STATUSES.map(
                        (
                          status
                        ) => (
                          <option
                            key={
                              status
                            }
                            value={
                              status
                            }
                          >
                            {status}
                          </option>
                        )
                      )}
                    </select>

                    {updatingOrderId ===
                      order.orderId && (
                      <span>
                        {" "}
                        Updating...
                      </span>
                    )}
                  </div>

                  <h4>
                    Order Items
                  </h4>

                  {order.items.map(
                    (
                      item
                    ) => (
                      <div
                        key={
                          item.orderItemId
                        }
                      >
                        <p>
                          <strong>
                            {
                              item.productName
                            }
                          </strong>
                        </p>

                        <p>
                          Price: €
                          {Number(
                            item.unitPrice
                          ).toFixed(
                            2
                          )}
                        </p>

                        <p>
                          Quantity:{" "}
                          {
                            item.quantity
                          }
                        </p>

                        <p>
                          Subtotal: €
                          {Number(
                            item.subtotal
                          ).toFixed(
                            2
                          )}
                        </p>
                      </div>
                    )
                  )}

                  <hr />
                </div>
              )
            )}
        </section>
      </main>
    </div>
  );
}

export default AdminDashboardPage;