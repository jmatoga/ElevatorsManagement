import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";
import "./App.css";
import Footer from "./footer/Footer";
import Navbar from "./navbar/Navbar";
import ElevatorsListPanel from "./elevatorsListPanel/ElevatorsListPanel";
import LoginPanel from "./loginPanel/LoginPanel";
import Cookies from "js-cookie";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!Cookies.get("accessToken"));

  useEffect(() => {
    setIsLoggedIn(!!Cookies.get("accessToken"));
  }, []);

  const renderLoginPage = () => {
    return isLoggedIn ? (
      <Navigate to="/" />
    ) : (
      <LoginPanel onLogin={setIsLoggedIn} />
    );
  };

  const renderMainPage = () => {
    return isLoggedIn ? <ElevatorsListPanel /> : <Navigate to="/login" />;
  };

  return (
    <Router>
      <div className="d-flex flex-column min-vh-100">
        {isLoggedIn && <Navbar className="header" onLogin={setIsLoggedIn} />}
        <div className="flex-grow-1">
          <Routes>
            <Route path="/login" element={renderLoginPage()} />
            <Route path="/" element={renderMainPage()} />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </div>
        <Footer className="footer" />
      </div>
    </Router>
  );
}

export default App;
