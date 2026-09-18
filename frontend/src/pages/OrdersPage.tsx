import {
  useEffect,
  useState,
} from "react";

import Navbar from "../components/Navbar";

import {
  getOrders,
} from "../services/orderService";

import type {
  Order,
} from "../types/order";

function OrdersPage() {
  const [orders, setOrders] =
    useState<Order[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const data =
          await getOrders();

        setOrders(data);
      } catch (error) {
        console.error(
          "ORDER HISTORY ERROR:",
          error
        );

        setError(
          "Failed to load order history."
        );
      } finally {
        setLoading(false);
      }
    };

    loadOrders();
  }, []);

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
        <h1>Order History</h1>

        {loading && (
          <p>
            Loading orders...
          </p>
        )}

        {error && (
          <p>
            {error}
          </p>
        )}

        {!loading &&
          !error &&
          orders.length === 0 && (
            <p>
              You have not placed
              any orders yet.
            </p>
          )}

        {!loading &&
          !error &&
          orders.map(
            (order) => (
              <div
                key={
                  order.orderId
                }
              >
                <h2>
                  Order #
                  {
                    order.orderId
                  }
                </h2>

                <p>
                  <strong>
                    Status:
                  </strong>{" "}
                  {order.status}
                </p>

                <p>
                  <strong>
                    Order Date:
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

                <h3>
                  Products
                </h3>

                {order.items.map(
                  (item) => (
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
                        ).toFixed(2)}
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
                        ).toFixed(2)}
                      </p>
                    </div>
                  )
                )}

                <hr />
              </div>
            )
          )}
      </main>
    </div>
  );
}

export default OrdersPage;