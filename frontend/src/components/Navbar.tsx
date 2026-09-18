import {
  Link,
  useNavigate,
} from "react-router-dom";

function Navbar() {
  const navigate =
    useNavigate();

  const name =
    localStorage.getItem(
      "name"
    );

  const role =
    localStorage.getItem(
      "role"
    );

  const handleLogout = () => {
    localStorage.removeItem(
      "token"
    );

    localStorage.removeItem(
      "role"
    );

    localStorage.removeItem(
      "name"
    );

    navigate("/login");
  };

  return (
    <nav>
      <h2>
        OrderFlow
      </h2>

      <div>
        <Link to="/products">
          Products
        </Link>

        {role === "CUSTOMER" && (
          <>
            {" | "}

            <Link to="/cart">
              Cart
            </Link>

            {" | "}

            <Link to="/orders">
              Orders
            </Link>
          </>
        )}

        {role === "ADMIN" && (
          <>
            {" | "}

            <Link to="/admin">
              Admin Dashboard
            </Link>
          </>
        )}

        {name && (
          <span>
            {" "} | {name}
          </span>
        )}

        {role && (
          <span>
            {" "} ({role})
          </span>
        )}

        {" "}

        <button
          type="button"
          onClick={
            handleLogout
          }
        >
          Logout
        </button>
      </div>

      <hr />
    </nav>
  );
}

export default Navbar;