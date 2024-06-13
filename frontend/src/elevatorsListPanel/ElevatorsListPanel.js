import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { ButtonGroup, Button } from "react-bootstrap";

export default function ElevatorsListPanel() {
  axios.defaults.headers.post["Content-Type"] = "application/json";
  axios.defaults.headers.put["Content-Type"] = "application/json";

  const [elevators, setElevators] = useState([]);
  const [user, setUser] = useState([]);

  const doSomethingEveryThreeSeconds = () => {
    // Tutaj umieść kod, który ma być wykonywany co 3 sekundy
    console.log("Wykonuję co 3 sekundy");
    fetchData();
    fetchUserData();
    // Ustawienie nowego timeout po zakończeniu aktualnego
    setTimeout(doSomethingEveryThreeSeconds, 2000);
  };

  useEffect(() => {
    fetchData();
    fetchUserData();
    doSomethingEveryThreeSeconds();
    // const interval = setInterval(() => {
    //   fetchData();
    //   fetchUserData();
    //   console.log("Wykonuję co 3 sekundy");
    // }, 3000);
  }, []);

  const fetchData = async () => {
    try {
      const elevatorsResponse = await axios.get(
        `http://localhost:8090/api/elevator/all`,
        { withCredentials: true }
      );
      const elevatorsData = elevatorsResponse.data.map((data) => ({
        id: data.id,
        number: data.number,
        currentFloor: data.currentFloor,
        destinationsFloor: data.destinationsFloor,
        currentDirection: data.currentDirection,
        status: data.status,
        usersCount: data.usersCount,
      }));
      setElevators(elevatorsData);
      console.log(
        "!" + elevatorsData[4].number + elevatorsData[4].destinationsFloor
      );
      // console.log(elevators);
    } catch (error) {
      console.error("Error loading data:", error);
    }
  };

  const fetchUserData = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8090/api/current-user`,
        { withCredentials: true }
      );
      setUser(response.data);
    } catch (error) {
      console.error("Error loading data:", error);
    }
  };

  const handleUpRequest = useCallback((id) => {
    axios
      .post(
        `http://localhost:8090/api/elevator/update/${id.toString()}`,
        {},
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        setElevators((prevElevators) =>
          prevElevators.map((elevator) =>
            elevator.id === id ? { ...elevator, ...response.data } : elevator
          )
        );
        console.log(elevators);
      })
      .catch((error) => {
        console.error("Error updating elevator:", error);
      });
  }, []);

  const handleDownRequest = useCallback((id) => {
    axios
      .post(
        `http://localhost:8090/api/elevator/update/${id.toString()}`,
        {},
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        setElevators((prevElevators) =>
          prevElevators.map((elevator) =>
            elevator.id === id ? { ...elevator, ...response.data } : elevator
          )
        );
        console.log(elevators);
      })
      .catch((error) => {
        console.error("Error updating elevator:", error);
      });
  }, []);

  const handleFloorRequest = useCallback((id, floor) => {
    axios
      .post(
        `http://localhost:8090/api/elevator/floor/${id.toString()}`,
        floor,
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        setElevators((prevElevators) =>
          prevElevators.map((elevator) =>
            elevator.id === id ? { ...elevator, ...response.data } : elevator
          )
        );
      })
      .catch((error) => {
        console.error("Error updating elevator:", error);
      });
  }, []);

  const [selectedOptions, setSelectedOptions] = useState({});
  const [clickedButtons, setClickedButtons] = useState({});

  const handleSelect = (elevatorId, option) => {
    setSelectedOptions({
      ...selectedOptions,
      [elevatorId]: option,
    });
    if (option !== user.currentFloor) {
      handleFloorRequest(elevatorId, option);
    }
  };

  const handleButtonClick = (elevatorId) => {
    setClickedButtons({
      ...clickedButtons,
      [elevatorId]: true,
    });
  };

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-3 order-md-1 border rounded p-4 mt-2 shadow h-100">
          <h5 className="text-center mb-4">Details</h5>
          <h3>
            <b>Account owner:</b>
            <br />
            {user.name + " " + user.surname}
          </h3>
          <h4>
            Your current floor: {user.currentFloor}
            <br />
            <br />
          </h4>
        </div>

        <div className="col-md-9">
          <div className="py-4">
            <table className="table border shadow">
              <thead>
                <tr>
                  <th scope="col">Elevator No.</th>
                  <th scope="col">Current elevator floor</th>
                  <th scope="col">Destination floors</th>
                  <th scope="col">Current direction</th>
                  <th scope="col">People inside count</th>
                  <th scope="col">Status</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {elevators.map((elevator, index) => (
                  <tr key={index}>
                    <td>
                      <b>{elevator.number}</b>
                    </td>
                    <td>{elevator.currentFloor}</td>
                    <td>
                      {elevator.destinationsFloor.length === 0
                        ? "-----"
                        : elevator.destinationsFloor.join(", ")}
                    </td>
                    <td>{elevator.currentDirection}</td>
                    <td>{elevator.usersCount}</td>
                    <td>
                      <i>{elevator.status}</i>
                    </td>
                    <td>
                      {elevator.currentFloor === user.currentFloor &&
                      clickedButtons[elevator.id] ? (
                        <ButtonGroup>
                          {[0, 1, 2, 3, 4, 5].map((option) => (
                            <Button
                              key={option}
                              variant={
                                selectedOptions[elevator.id] === option
                                  ? "primary"
                                  : "outline-primary"
                              }
                              onClick={() => handleSelect(elevator.id, option)}
                            >
                              Floor: {option}
                            </Button>
                          ))}
                        </ButtonGroup>
                      ) : (
                        <>
                          <form
                            className="submitUP"
                            onSubmit={(event) => {
                              event.preventDefault();
                              handleUpRequest(elevator.id);
                              handleButtonClick(elevator.id);
                            }}
                          >
                            <button
                              type="submit"
                              className={`btn mx-1 ${
                                clickedButtons[elevator.id] === "up"
                                  ? "btn-success"
                                  : "btn-primary"
                              }`}
                            >
                              Up
                            </button>
                          </form>
                          <form
                            className="submitDOWN"
                            onSubmit={(event) => {
                              event.preventDefault();
                              handleDownRequest(elevator.id);
                              handleButtonClick(elevator.id);
                            }}
                          >
                            <button
                              type="submit"
                              className={`btn mx-1 ${
                                clickedButtons[elevator.id] === "down"
                                  ? "btn-success"
                                  : "btn-primary"
                              }`}
                            >
                              Down
                            </button>
                          </form>
                        </>
                      )}
                      {/* <Link
                        className="btn btn-outline-primary mx-1"
                        to={`/editCampaign/${elevator.id}`}
                      >
                        Edit
                      </Link>
                      <Link
                        className="btn btn-danger mx-1"
                        onClick={() => handleUpRequest(elevator.id)}
                      >
                        Delete
                      </Link> */}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
