import {BrowserRouter, Route, Routes} from "react-router-dom";
import CheckMailPage from "./pages/CheckMailPage";
import {UserAccountPage} from "./pages/UserAccountPage";
import {Home} from "./pages/Home";


export function Router() {
    return <BrowserRouter>
        <Routes>
            <Route element={<Home/>} path='/' />
            <Route element={<CheckMailPage/>} path='/checkMailPage'/>
            <Route element={<UserAccountPage/>} path='/userAccount'/>
        </Routes>
    </BrowserRouter>
}