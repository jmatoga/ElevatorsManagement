import React from "react";
import { Link, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";

export default function Navbar({ onLogin }) {
  let navigate = useNavigate();
  const handleLogout = (e) => {
    e.preventDefault();
    const allCookies = Cookies.get();
    for (let cookie in allCookies) {
      Cookies.remove(cookie);
    }
    onLogin(false);
    navigate("/");
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">
            Elevators Management
          </Link>
          <div className="ml-auto">
            <form action="" onSubmit={handleLogout}>
              <button type="submit" className="btn btn-outline-danger">
                Log out
              </button>
            </form>
          </div>
        </div>
      </nav>
    </div>
  );
}
