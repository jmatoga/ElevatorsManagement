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
import UserPanel from "./userPanel/UserPanel";
// import ContactPanel from "./contactPanel/ContactPanel";
// import FaqPanel from "./faqPanel/FaqPanel";
// import HistoryPanel from "./historyPanel/HistoryPanel";
// import AchievementsPanel from "./achievementsPanel/AchievementsPanel";
import RegisterPage from "./loginPanel/RegisterPanel";
import Cookies from "js-cookie";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";

function App() {
  // const cors = require("cors");
  // const corsOptions = {
  //   origin: "http://localhost:3000",
  //   credentials: true, //access-control-allow-credentials:true
  //   optionSuccessStatus: 200,
  // };
  // App.use(cors(corsOptions));

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

  const renderRegisterPage = () => {
    return isLoggedIn ? <Navigate to="/" /> : <RegisterPage />;
  };

  const renderUserPanel = () => {
    return isLoggedIn ? (
      <UserPanel onLogin={setIsLoggedIn} />
    ) : (
      <Navigate to="/" />
    );
  };

  // const renderAchievementsPage = () => {
  //   return isLoggedIn ? <AchievementsPanel /> : <Navigate to="/" />;
  // };

  // const renderContactPage = () => {
  //   return isLoggedIn ? <ContactPanel /> : <Navigate to="/" />;
  // };

  // const renderFaqPage = () => {
  //   return isLoggedIn ? <FaqPanel /> : <Navigate to="/" />;
  // };

  // const renderHistoryPage = () => {
  //   return isLoggedIn ? <HistoryPanel /> : <Navigate to="/" />;
  // };

  return (
    <Router>
      <div className="d-flex flex-column min-vh-100">
        {isLoggedIn && <Navbar className="header" />}
        <div className="flex-grow-1">
          <Routes>
            <Route path="/login" element={renderLoginPage()} />
            <Route path="/" element={renderMainPage()} />
            <Route path="/register" element={renderRegisterPage()} />
            <Route path="/details" element={renderUserPanel()} />
            {/* <Route path="/achievements" element={renderAchievementsPage()} />
          <Route path="/contact" element={renderContactPage()} />
          <Route path="/faq" element={renderFaqPage()} />
          <Route path="/history" element={renderHistoryPage()} /> */}
            {/* <Route path="*" element={<Navigate to="/" />} /> */}
          </Routes>
        </div>
        <Footer className="footer" />
      </div>
    </Router>
  );
}

export default App;
