import {
  useEffect,
  useState,
} from "react";

import Navbar from "../components/Navbar";

import {
  clearCart,
  getCart,
  removeCartItem,
  updateCartItem,
} from "../services/cartService";

import {
  checkout,
} from "../services/orderService";

import type {
  CartItem,
} from "../types/cart";

import type {
  Order,
} from "../types/order";

function CartPage() {
  const [cartItems, setCartItems] =
    useState<CartItem[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");

  const [
    updatingItemId,
    setUpdatingItemId,
  ] = useState<number | null>(null);

  const [clearing, setClearing] =
    useState(false);

  const [
    checkingOut,
    setCheckingOut,
  ] = useState(false);

  const [
    completedOrder,
    setCompletedOrder,
  ] = useState<Order | null>(null);

  useEffect(() => {
    const loadCart = async () => {
      try {
        const data =
          await getCart();

        setCartItems(data);
      } catch (error) {
        console.error(
          "CART ERROR:",
          error
        );

        setError(
          "Failed to load cart."
        );
      } finally {
        setLoading(false);
      }
    };

    loadCart();
  }, []);

  const handleQuantityChange =
    async (
      item: CartItem,
      newQuantity: number
    ) => {

      if (newQuantity < 1) {
        return;
      }

      setError("");
      setCompletedOrder(null);

      setUpdatingItemId(
        item.cartItemId
      );

      try {
        const updatedItem =
          await updateCartItem(
            item.cartItemId,
            newQuantity
          );

        setCartItems(
          (currentItems) =>
            currentItems.map(
              (currentItem) =>
                currentItem.cartItemId ===
                updatedItem.cartItemId
                  ? updatedItem
                  : currentItem
            )
        );
      } catch (error) {
        console.error(
          "UPDATE CART ERROR:",
          error
        );

        setError(
          "Failed to update quantity."
        );
      } finally {
        setUpdatingItemId(null);
      }
    };

  const handleRemove =
    async (
      cartItemId: number
    ) => {

      setError("");
      setCompletedOrder(null);

      setUpdatingItemId(
        cartItemId
      );

      try {
        await removeCartItem(
          cartItemId
        );

        setCartItems(
          (currentItems) =>
            currentItems.filter(
              (item) =>
                item.cartItemId !==
                cartItemId
            )
        );
      } catch (error) {
        console.error(
          "REMOVE CART ITEM ERROR:",
          error
        );

        setError(
          "Failed to remove item."
        );
      } finally {
        setUpdatingItemId(null);
      }
    };

  const handleClearCart =
    async () => {

      setError("");
      setCompletedOrder(null);
      setClearing(true);

      try {
        await clearCart();

        setCartItems([]);
      } catch (error) {
        console.error(
          "CLEAR CART ERROR:",
          error
        );

        setError(
          "Failed to clear cart."
        );
      } finally {
        setClearing(false);
      }
    };

  const handleCheckout =
    async () => {

      setError("");
      setCompletedOrder(null);
      setCheckingOut(true);

      try {
        const order =
          await checkout();

        setCompletedOrder(order);

        /*
         * Backend clears the cart after
         * successful checkout.
         */
        setCartItems([]);
      } catch (error) {
        console.error(
          "CHECKOUT ERROR:",
          error
        );

        setError(
          "Checkout failed. Please check product availability and try again."
        );
      } finally {
        setCheckingOut(false);
      }
    };

  const totalPrice =
    cartItems.reduce(
      (total, item) =>
        total +
        Number(item.subtotal),
      0
    );

  const totalQuantity =
    cartItems.reduce(
      (total, item) =>
        total +
        item.quantity,
      0
    );

  return (
    <div>
      <Navbar />

      <main>
        <h1>Shopping Cart</h1>

        {loading && (
          <p>
            Loading cart...
          </p>
        )}

        {error && (
          <p>
            {error}
          </p>
        )}

        {completedOrder && (
          <div>
            <h2>
              Order Confirmed
            </h2>

            <p>
              Your order was placed
              successfully.
            </p>

            <p>
              <strong>
                Order ID:
              </strong>{" "}
              #
              {
                completedOrder.orderId
              }
            </p>

            <p>
              <strong>
                Status:
              </strong>{" "}
              {
                completedOrder.status
              }
            </p>

            <p>
              <strong>
                Total:
              </strong>{" "}
              €
              {Number(
                completedOrder.totalAmount
              ).toFixed(2)}
            </p>
          </div>
        )}

        {!loading &&
          !completedOrder &&
          cartItems.length === 0 && (
            <div>
              <p>
                Your cart is empty.
              </p>
            </div>
          )}

        {!loading &&
          cartItems.length > 0 && (
            <div>
              <p>
                <strong>
                  Total Items:
                </strong>{" "}
                {totalQuantity}
              </p>

              {cartItems.map(
                (item) => {

                  const isUpdating =
                    updatingItemId ===
                    item.cartItemId;

                  return (
                    <div
                      key={
                        item.cartItemId
                      }
                    >
                      <h3>
                        {
                          item.productName
                        }
                      </h3>

                      <p>
                        <strong>
                          Price:
                        </strong>{" "}
                        €
                        {Number(
                          item.price
                        ).toFixed(2)}
                      </p>

                      <p>
                        <strong>
                          Quantity:
                        </strong>{" "}
                        {
                          item.quantity
                        }
                      </p>

                      <div>
                        <button
                          type="button"
                          disabled={
                            isUpdating ||
                            item.quantity <=
                              1 ||
                            checkingOut
                          }
                          onClick={() =>
                            handleQuantityChange(
                              item,
                              item.quantity -
                                1
                            )
                          }
                        >
                          −
                        </button>

                        {" "}

                        <span>
                          {
                            item.quantity
                          }
                        </span>

                        {" "}

                        <button
                          type="button"
                          disabled={
                            isUpdating ||
                            checkingOut
                          }
                          onClick={() =>
                            handleQuantityChange(
                              item,
                              item.quantity +
                                1
                            )
                          }
                        >
                          +
                        </button>

                        {" "}

                        <button
                          type="button"
                          disabled={
                            isUpdating ||
                            checkingOut
                          }
                          onClick={() =>
                            handleRemove(
                              item.cartItemId
                            )
                          }
                        >
                          {isUpdating
                            ? "Updating..."
                            : "Remove"}
                        </button>
                      </div>

                      <p>
                        <strong>
                          Subtotal:
                        </strong>{" "}
                        €
                        {Number(
                          item.subtotal
                        ).toFixed(2)}
                      </p>

                      <hr />
                    </div>
                  );
                }
              )}

              <h2>
                Total: €
                {totalPrice.toFixed(
                  2
                )}
              </h2>

              <button
                type="button"
                disabled={
                  clearing ||
                  checkingOut
                }
                onClick={
                  handleClearCart
                }
              >
                {clearing
                  ? "Clearing..."
                  : "Clear Cart"}
              </button>

              {" "}

              <button
                type="button"
                disabled={
                  checkingOut ||
                  clearing
                }
                onClick={
                  handleCheckout
                }
              >
                {checkingOut
                  ? "Processing..."
                  : "Checkout"}
              </button>
            </div>
          )}
      </main>
    </div>
  );
}

export default CartPage;