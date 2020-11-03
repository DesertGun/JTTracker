<template>
  <b-container fluid>
    <b-row>
      <b-col />
      <b-col>
        <h3>
          Test
        </h3>
        <div v-if="responseSuccess">
          <b-alert show variant="success">
            {{ responseSuccess }}
          </b-alert>
          <div v-if="changed">
            <b-button variant="success" @click="login()">
              LogIn
            </b-button>
          </div>
          <div v-else>
            <b-form-group id="resetFormGroup" description="Please type in your new password" label-for="resetpw">
              <p>
                Please enter your new password
              </p>
              <b-form-input id="resetpw" v-model="pw1" type="password" />
              <b-form-input id="resetpwvalidation" v-model="pw2" type="password" />
              <b-form-invalid-feedback :state="validationPasswordEq">
                Your passwords dont match!
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationPasswordEq">
                Passwords match!
              </b-form-valid-feedback>
              <b-form-invalid-feedback :state="validationPassword">
                Your password needs to be at least 8 chars long!
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationPassword">
                Looks Good.
              </b-form-valid-feedback>
              <div v-if="validationPassword && validationPasswordEq">
                <b-button variant="primary" @click="submit()">
                  Reset
                </b-button>
              </div>
            </b-form-group>
          </div>
        </div>
        <div v-else>
          <b-alert dismissible show variant="danger">
            {{ responseError }}
          </b-alert>
        </div>
      </b-col>
      <b-col />
    </b-row>
  </b-container>
</template>

<script>
export default {
  computed: {
    validationPasswordEq () {
      return this.pw1 === this.pw2
    },
    validationPassword () {
      return this.pw1.length >= 8
    }
  },
  asyncData () {
    return {
      token: '',
      user: '',
      pw1: '',
      pw2: '',
      responseSuccess: '',
      responseError: '',
      changed: false
    }
  },
  async mounted () {
    try {
      const token = this.$route.query.token
      const user = this.$route.query.username
      this.user = user
      this.token = token
      const response = await this.$axios.post('/user/password/reset/validate', { username: this.user, passwordResetToken: this.token })
      if (response.data.validated === true) {
        this.responseSuccess = response.data.successMessage
      } else {
        this.responseError = response.data.errorMessage
      }
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    async submit () {
      // TODO: change API-Links in accordance to best-practise!
      try {
        const response = await this.$axios.post('/user/password/reset/change', { username: this.user, newPassword: this.pw2 })
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
          this.changed = true
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    login () {
      this.$router.push('/login')
    }
  }
}
</script>
