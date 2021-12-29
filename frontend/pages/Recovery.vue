<template>
  <div class="main">
    <b-container fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Recover your password</h3>
          </div>
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
                {{ securityQuestion1 }}
                <b-form-input
                  v-model="securityAnswer1"
                  class="mt-2"
                ></b-form-input>
                {{ securityQuestion2 }}
                <b-form-input
                  v-model="securityAnswer2"
                  class="mt-2"
                ></b-form-input>
                {{ securityQuestion3 }}
                <b-form-input
                  v-model="securityAnswer3"
                  class="mt-2"
                ></b-form-input>
              </div>
              <div v-if="securityAnswer1 && securityAnswer2 && securityAnswer3">
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
  name: 'RecoveryPage',
  asyncData() {
    return {
      recoveryEmail: '',
      responseSuccess: '',
      responseError: '',
      responseValidation: '',
      securityQuestion1: null,
      securityQuestion2: null,
      securityQuestion3: null,
      securityAnswer1: null,
      securityAnswer2: null,
      securityAnswer3: null,
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
          this.securityQuestion1 = securityResponse.data.securityQuestion1
          this.securityQuestion2 = securityResponse.data.securityQuestion2
          this.securityQuestion3 = securityResponse.data.securityQuestion3
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    async validateSecurityAnswer() {
      try {
        const response = await this.$axios.post('/security', {
          username: this.recoveryEmail,
          securityAnswer1: this.securityAnswer1,
          securityAnswer2: this.securityAnswer2,
          securityAnswer3: this.securityAnswer3,
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
