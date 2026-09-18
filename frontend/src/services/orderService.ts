import api from "./api";

import type {
  Order,
} from "../types/order";

export const checkout =
  async (): Promise<Order> => {

    const response =
      await api.post<Order>(
        "/orders/checkout"
      );

    return response.data;
  };

export const getOrders =
  async (): Promise<Order[]> => {

    const response =
      await api.get<Order[]>(
        "/orders"
      );

    return response.data;
  };

export const getAllAdminOrders =
  async (): Promise<Order[]> => {

    const response =
      await api.get<Order[]>(
        "/admin/orders"
      );

    return response.data;
  };

export const updateOrderStatus =
  async (
    orderId: number,
    status: string
  ): Promise<Order> => {

    const response =
      await api.put<Order>(
        `/admin/orders/${orderId}/status`,
        {
          status,
        }
      );

    return response.data;
  };