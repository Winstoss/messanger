import { useState } from "react";
import Login from "./Login";
import { Registration } from "./Registration";

export function LoginPage() {
    
  const [registration, setRegistration] = useState(false);

  return (
    <div>
      {registration ? (
        <Registration setRegistration={()=>(setRegistration(false))} />
      ) : (
        <Login setRegistration={()=>(setRegistration(true))} />
      )}
    </div>
  );
}
