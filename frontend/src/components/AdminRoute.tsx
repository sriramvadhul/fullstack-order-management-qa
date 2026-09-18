import type {
  ReactNode,
} from "react";

import {
  Navigate,
} from "react-router-dom";

interface AdminRouteProps {
  children: ReactNode;
}

function AdminRoute({
  children,
}: AdminRouteProps) {

  const token =
    localStorage.getItem(
      "token"
    );

  const role =
    localStorage.getItem(
      "role"
    );

  if (!token) {
    return (
      <Navigate
        to="/login"
        replace
      />
    );
  }

  if (role !== "ADMIN") {
    return (
      <Navigate
        to="/products"
        replace
      />
    );
  }

  return children;
}

export default AdminRoute;