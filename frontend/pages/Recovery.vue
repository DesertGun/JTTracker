<template>
  <div class="main">
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <h3>Recover your password</h3>
          <div v-if="responseSuccess" />
          <div v-else>
            <b-form-group
              id="recoveryFormGroup"
              description="Please enter your email"
              label-for="recoverymail"
            >
              <p>
                Please enter your E-Mail-Adress, with which you registered in
                order to receive a link with which you can reset your password!
              </p>
              <b-form-input
                id="recoverymail"
                v-model="recoveryEmail"
                type="email"
              />
              <br />
              <b-button variant="danger" @click="recoverPassword()">
                Please send me a password-reset-link !
              </b-button>
            </b-form-group>
          </div>
          <p>
            Please keep in mind, that the reset-token remains valid for 1 hour
            max.!
          </p>
          <div v-if="responseSuccess" class="pt-2" style="text-align: center">
            <b-alert show variant="success">
              {{ responseMassage }}
              <p>
                A recovery-link was send to your E-mail-Adress. Just open the
                link in your Browser and you can reset your password!
              </p>
            </b-alert>
          </div>
          <div v-if="responseError" class="pt-2" style="text-align: center">
            <b-alert dismissible show variant="danger">
              {{ responseError }}
            </b-alert>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  asyncData() {
    return {
      recoveryEmail: '',
      responseSuccess: '',
      responseError: '',
    }
  },
  methods: {
    async recoverPassword() {
      try {
        const response = await this.$axios.post('/user/password/reset', {
          username: this.recoveryEmail,
        })

        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
  },
}
</script>
