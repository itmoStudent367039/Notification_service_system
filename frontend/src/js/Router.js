import {BrowserRouter, Route, Routes} from "react-router-dom";
import CheckMailPage from "./pages/CheckMailPage";
import {UserAccountPage} from "./pages/UserAccountPage";
import {Home} from "./pages/Home";
import {AccountPage} from "./pages/AccountPage";
import {InformationPage} from "./pages/InformationPage";
import {SubscribePage} from "./pages/SubscribePage";


export function Router() {
    return <BrowserRouter>
        <Routes>
            <Route element={<Home/>} path='/' />
            <Route element={<CheckMailPage/>} path='/checkMailPage'/>
            <Route element={<UserAccountPage/>} path='/userAccount'/>
            <Route element={<AccountPage/>} path='/account'/>
            <Route element={<InformationPage/>} path='/info'/>
            <Route element={<SubscribePage/>} path='/subscribe'/>
        </Routes>
    </BrowserRouter>
}