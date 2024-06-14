import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { ButtonGroup, Button } from "react-bootstrap";

export default function ElevatorsListPanel() {
  axios.defaults.headers.post["Content-Type"] = "application/json";
  axios.defaults.headers.put["Content-Type"] = "application/json";

  const [elevators, setElevators] = useState([]);
  const [user, setUser] = useState({});

  const doSomethingEveryThreeSeconds = () => {
    fetchData();
    fetchUserData();

    setTimeout(doSomethingEveryThreeSeconds, 2000);
  };

  useEffect(() => {
    fetchData();
    fetchUserData();
    doSomethingEveryThreeSeconds();
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
        usersInsideElevator: data.usersInsideElevator,
      }));
      setElevators(elevatorsData);
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
      if (
        response.data.insideElevator !== user.insideElevator &&
        response.data.insideElevator === false
      ) {
        setSelectedOptions({});
      }
      setUser(response.data);
    } catch (error) {
      console.error("Error loading data:", error);
    }
  };

  const handleUpDownRequest = useCallback((id) => {
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
    if (
      elevators.find((elevator) => elevator.id === elevatorId).usersCount >= 3
    ) {
      alert(
        "Elevator is full, choose other elevator or wait for the next one."
      );
      return;
    }
    if (option !== user.currentFloor) {
      handleFloorRequest(elevatorId, option);
    }
  };

  const handleButtonClick = (elevatorId, direction) => {
    setClickedButtons({
      ...clickedButtons,
      [elevatorId]: direction,
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
            Your current floor: <b>{user.currentFloor}</b> <br />
            Is inside elevator: <b>{user.insideElevator ? "Yes" : "No"}</b>
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
                  <th scope="col">People inside elevator</th>
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
                      {(clickedButtons[elevator.id] &&
                        elevator.currentFloor === user.currentFloor) ||
                      (Array.isArray(elevator.usersInsideElevator) &&
                        elevator.usersInsideElevator.includes(user.id)) ? (
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
                      ) : !user.insideElevator ? (
                        <>
                          <form
                            className="submitUP"
                            onSubmit={(event) => {
                              event.preventDefault();
                              handleUpDownRequest(elevator.id);
                              handleButtonClick(elevator.id, "up");
                            }}
                          >
                            <button
                              type="submit"
                              className={`btn mx-1 btn-primary`}
                            >
                              Up
                            </button>
                          </form>
                          <form
                            className="submitDOWN"
                            onSubmit={(event) => {
                              event.preventDefault();
                              handleUpDownRequest(elevator.id);
                              handleButtonClick(elevator.id, "down");
                            }}
                          >
                            <button
                              type="submit"
                              className={`btn mx-1 btn-primary`}
                            >
                              Down
                            </button>
                          </form>
                        </>
                      ) : null}
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
