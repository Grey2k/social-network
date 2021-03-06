import { configureStore } from '@reduxjs/toolkit';
import usersReducer from '../features/users/usersSlice';
import applicationReducer from '../features/application/applicationSlice';
import authReducer from '../features/auth/authSlice';
import profileReducer from '../features/profile/profileSlice'
import { friendsSlice } from '../features/friends/friendsSlice';

export default configureStore({
  reducer: {
    users: usersReducer,
    application: applicationReducer,
    auth: authReducer,
    profile: profileReducer,
    friends: friendsSlice.reducer
  }
});
