<template>
  <div class="profileMain">
    <b-container class="profileHeaderContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <div style="text-align: center">
            <h3>Profile</h3>
          </div>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
    <b-container class="profileContentContainer" fluid>
      <b-row>
        <b-col />
        <b-col>
          <b-form>
            <b-form-group
              id="usernameFormGroup"
              description="Keep in mind, that your Username is your contact adress!"
              label="Username:"
              label-for="username"
            >
              <b-form-input
                id="username"
                v-model="username"
                disabled="true"
                type="text"
              />
            </b-form-group>
            <b-form-group
              id="accountNameFormGroup"
              description="With the following name you will be visible for other users. The choice is up to you. Default is your username."
              label="Account name:"
              label-for="accountName"
            >
              <b-form-input
                id="accountName"
                v-model="accountName"
                placeholder="None"
                type="text"
              />
              <b-form-invalid-feedback :state="validationaccountNameChange">
                Your global name cannot be empty!
              </b-form-invalid-feedback>
              <b-form-invalid-feedback :state="validationaccountNameNew">
                No changes detected !
              </b-form-invalid-feedback>
              <b-form-valid-feedback :state="validationaccountNameChange" />
            </b-form-group>
            <b-row>
              <b-col />
              <b-col>
                <b-form-group
                  id="avatarFormGroup"
                  description="Your personal Account-Avatar"
                  label="Avatar:"
                  label-for="avatar"
                >
                  <div v-if="!hasProfilePicture">
                    <b-img-lazy
                      :src="
                        'https://gravatar.com/avatar/' +
                        getHash +
                        '?d=identicon'
                      "
                      rounded="circle"
                    />
                  </div>
                  <div v-else>
                    <b-img-lazy
                      :src="getProfilePicture"
                      height="150px"
                      width="150px"
                      rounded="circle"
                    />
                  </div>
                </b-form-group>
              </b-col>
              <b-col />
            </b-row>
            <b-row>
              <b-col />
              <b-col cols="12">
                <b-form-file
                  v-model="profilePictureUpload"
                  class="mb-2"
                  accept=".jpg"
                ></b-form-file>
                <div v-if="uploadCurrent > 0">
                  <b-progress
                    :value="uploadCurrent"
                    :max="uploadMax"
                    show-progress
                    animated
                  ></b-progress>
                </div>
                <b-row>
                  <b-col cols="3" />
                  <b-col>
                    <div v-if="profilePictureUpload">
                      <b-button
                        variant="secondary"
                        class="mt-2"
                        @click="uploadProfilePicture"
                      >
                        Upload my profile picture !
                      </b-button>
                    </div>
                  </b-col>
                  <b-col cols="3" />
                </b-row>
                <b-row>
                  <b-col />
                  <b-col cols="10">
                    <div
                      v-if="responseSuccess"
                      class="pt-2"
                      style="text-align: center"
                    >
                      <b-alert dismissible show variant="success">
                        {{ responseSuccess }}
                      </b-alert>
                    </div>
                  </b-col>
                  <b-col />
                </b-row>
              </b-col>
            </b-row>
            <b-form-group
              id="passwordFormGroup"
              description="You will receive a password-reset link to the adress (username) provided above"
              label="Password:"
              label-for="password"
            >
            </b-form-group>
            <b-button variant="secondary" @click="changePassword">
              Change current password
            </b-button>
            <div
              v-if="
                validationaccountNameChange &&
                accountName !== username &&
                validationaccountNameNew
              "
              class="pt-2"
            >
              <b-button variant="success" @click="updateProfile">
                Update my profile
              </b-button>
            </div>
            <div v-if="updated" class="pt-2" style="text-align: center">
              <b-alert dismissible show variant="success">
                Your profile was updated successfully!
              </b-alert>
            </div>
            <div v-if="debugSec">
              <b-button
                variant="danger"
                class="mt-2"
                @click="removeEnchancedSecurity"
              >
                Disable enchanced Security for my Account !
              </b-button>
            </div>
            <div v-else>
              <b-form-group id="input-group-2fa" label-for="choice-2fa">
                <b-button variant="primary" class="mt-2" @click="openQuestions">
                  Enable enchanced Security for my Account !
                </b-button>
                <div v-if="enableSecurity">
                  <b-form-select
                    v-model="firstQuestion"
                    :options="questions"
                    class="mt-2"
                  ></b-form-select>
                  <div v-if="firstQuestion">
                    <b-form-input
                      v-model="firstAnswer"
                      class="mt-2"
                    ></b-form-input>
                  </div>
                  <div v-if="firstAnswer">
                    <b-form-select
                      v-model="secondQuestion"
                      :options="questions"
                      class="mt-2"
                    ></b-form-select>
                  </div>
                  <div v-if="secondQuestion">
                    <b-form-input
                      v-model="secondAnswer"
                      class="mt-2"
                    ></b-form-input>
                  </div>
                  <div v-if="secondAnswer">
                    <b-form-select
                      v-model="thirdQuestion"
                      :options="questions"
                      class="mt-2"
                    ></b-form-select>
                  </div>
                  <div v-if="thirdQuestion">
                    <b-form-input
                      v-model="thirdAnswer"
                      class="mt-2"
                    ></b-form-input>
                  </div>
                  <div v-if="thirdAnswer">
                    Now you can enable enchanced security for your account !
                    <b-row>
                      <b-col>
                        <b-button
                          variant="success"
                          class="mt-2"
                          @click="submitSecurityQuestions"
                        >
                          Submit
                        </b-button>
                      </b-col>
                      <b-col />
                      <b-col>
                        <b-button
                          variant="danger"
                          class="mt-2"
                          @click="closeSecurityQuestions"
                        >
                          Forget it
                        </b-button>
                      </b-col>
                    </b-row>
                  </div>
                </div>
              </b-form-group>
            </div>
          </b-form>
        </b-col>
        <b-col />
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  middleware: 'authenticated',
  asyncData() {
    return {
      username: null,
      accountName: '',
      initaccountName: '',
      dummyPassword: 'placeHolderPasswordForRenderingPurposes',
      changed: false,
      updated: false,
      profilePictureUpload: null,
      responseError: null,
      responseSuccess: null,
      uploadCurrent: 0,
      uploadMax: 100,
      enableSecurity: null,
      firstQuestion: null,
      secondQuestion: null,
      thirdQuestion: null,
      firstAnswer: null,
      secondAnswer: null,
      thirdAnswer: null,
      debugSec: null,
      questions: [
        { value: null, text: 'Select a question !' },
        {
          value: 'What is the name of your first pet?',
          text: 'What is the name of your first pet?',
        },
        {
          value: 'What is the name of your first school?',
          text: 'What is the name of your first school?',
        },
        { value: 'What was your first job?', text: 'What was your first job?' },
        {
          value: 'What is your favorite color?',
          text: 'What is your favorite color?',
        },
        {
          value: 'What is your favorite date in history?',
          text: 'What is your favorite date in history?',
        },
      ],
    }
  },
  computed: {
    validationaccountNameChange() {
      return this.accountName.length > 0
    },
    validationaccountNameNew() {
      return this.accountName !== this.initaccountName
    },
    ...mapGetters({
      getProfilePicture: 'user/getProfilePicture',
      getHash: 'user/getHash',
      hasProfilePicture: 'user/hasProfilePicture',
      isLoggedIn: 'auth/isLoggedIn',
      getUsername: 'user/getUsername',
      getAccountname: 'user/getAccountname',
      hasSecurityEnabled: 'user/hasSecurityEnabled',
    }),
  },
  mounted() {
    try {
      this.username = this.getUsername
      if (this.getAccountname) {
        this.accountName = this.getAccountname
        this.initaccountName = this.accountName
      } else {
        this.accountName = this.username
        this.initaccountName = this.accountName
      }
      this.hash = this.getHash
      this.debugSec = this.hasSecurityEnabled
    } catch (e) {
      alert(e.toString())
    }
  },
  methods: {
    removeEnchancedSecurity() {
      this.debugSec = false
      // TODO: add backend-logic for removal of 2FA
    },
    openQuestions() {
      this.enableSecurity = true
    },
    closeSecurityQuestions() {
      this.firstQuestion = null
      this.secondQuestion = null
      this.thirdQuestion = null
      this.firstAnswer = null
      this.secondAnswer = null
      this.thirdAnswer = null
      this.enableSecurity = false
    },
    submitSecurityQuestions() {
      this.debugSec = true
      this.closeSecurityQuestions()
      // TODO: add backend-logic for activation of 2FA
    },
    async updateProfile() {
      try {
        const response = await this.$axios.put('/user/update', {
          username: this.username,
          accountName: this.accountName,
        })
        if (response.data.validated === true) {
          this.updated = true
        }
        await this.$store.dispatch('user/setProfileData')
      } catch (e) {
        alert(e.toString())
      }
    },
    async uploadProfilePicture() {
      try {
        const formData = new FormData()
        formData.append('profilePicture', this.profilePictureUpload)

        const response = await this.$axios.post('/user/picture', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          onUploadProgress: function (progressEvent) {
            this.uploadCurrent = parseInt(
              Math.round((progressEvent.loaded / progressEvent.total) * 100)
            )
          }.bind(this),
        })
        this.profilePictureUpload = null
        if (response.data.validated === true) {
          this.responseSuccess = response.data.successMessage
          await this.$store.dispatch('user/setProfilePicture')
          this.uploadCurrent = 0
          this.profilePicture = this.getProfilePicture
        } else {
          this.responseError = response.data.errorMessage
        }
      } catch (e) {
        alert(e.toString())
      }
    },
    changePassword() {
      this.$router.push('/passwordChange')
    },
  },
}
</script>
