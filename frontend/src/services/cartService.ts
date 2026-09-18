import api from "./api";

import type {
  AddToCartRequest,
  CartItem,
} from "../types/cart";

export const addToCart = async (
  request: AddToCartRequest
): Promise<CartItem> => {
  const response = await api.post<CartItem>(
    "/cart",
    request
  );

  return response.data;
};

export const getCart = async (): Promise<CartItem[]> => {
  const response =
    await api.get<CartItem[]>("/cart");

  return response.data;
};

export const updateCartItem = async (
  cartItemId: number,
  quantity: number
): Promise<CartItem> => {
  const response = await api.put<CartItem>(
    `/cart/${cartItemId}`,
    {
      quantity,
    }
  );

  return response.data;
};

export const removeCartItem = async (
  cartItemId: number
): Promise<void> => {
  await api.delete(
    `/cart/${cartItemId}`
  );
};

export const clearCart = async (): Promise<void> => {
  await api.delete("/cart");
};