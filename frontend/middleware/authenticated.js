export default function ({ store, redirect }) {
  if (!store.state.auth.jwtToken) {
    return redirect('/login')
  }
}
