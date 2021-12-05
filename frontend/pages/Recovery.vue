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
              <div v-if="responseValidation">
                Please enter the answer for the security question !
                {{ securityQuestion_1 }}
                <b-form-input
                  v-model="securityAnswer_1"
                  class="mt-2"
                ></b-form-input>
                {{ securityQuestion_2 }}
                <b-form-input
                  v-model="securityAnswer_2"
                  class="mt-2"
                ></b-form-input>
                {{ securityQuestion_3 }}
                <b-form-input
                  v-model="securityAnswer_3"
                  class="mt-2"
                ></b-form-input>
              </div>
              <div
                v-if="securityAnswer_1 && securityAnswer_2 && securityAnswer_3"
              >
                <b-button
                  variant="primary"
                  class="mt-2"
                  @click="validateSecurityAnswer()"
                >
                  Validate security answer !
                </b-button>
              </div>
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
      responseValidation: '',
      securityQuestion_1: null,
      securityQuestion_2: null,
      securityQuestion_3: null,
      securityAnswer_1: null,
      securityAnswer_2: null,
      securityAnswer_3: null,
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
        } else if (
          response.data.validated === false &&
          response.data.errorMessage
        ) {
          this.responseError = response.data.errorMessage
        } else if (
          response.data.validated === false &&
          response.data.validationMessage
        ) {
          this.responseValidation = response.data.validationMessage
          const securityResponse = await this.$axios.get(
            '/security/' + this.recoveryEmail
          )
          this.securityQuestion_1 = securityResponse.data.securityQuestion_1
          this.securityQuestion_2 = securityResponse.data.securityQuestion_2
          this.securityQuestion_3 = securityResponse.data.securityQuestion_3
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    async validateSecurityAnswer() {
      try {
        const response = await this.$axios.post('/security', {
          username: this.recoveryEmail,
          securityAnswer_1: this.securityAnswer_1,
          securityAnswer_2: this.securityAnswer_2,
          securityAnswer_3: this.securityAnswer_3,
        })

        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
        } else if (this.responseError) {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
  },
}
</script>
