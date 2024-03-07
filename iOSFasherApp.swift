Constants.swift

import Foundation

struct Constants {
    
    struct Storyboard {
        
        static let homeViewController = "HomeViewController"
        
    }
    
    
}

Utilities.swift

import Foundation
import UIKit

class Utilities {
    
    static func styleTextField(_ textfield:UITextField) {
        
        // Create the bottom line
        let bottomLine = CALayer()
        
        bottomLine.frame = CGRect(x: 0, y: textfield.frame.height - 2, width: textfield.frame.width, height: 2)
        
        bottomLine.backgroundColor = UIColor.init(red: 48/255, green: 173/255, blue: 99/255, alpha: 1).cgColor
        
        // Remove border on text field
        textfield.borderStyle = .none
        
        // Add the line to the text field
        textfield.layer.addSublayer(bottomLine)
        
    }
    
    static func styleFilledButton(_ button:UIButton) {
        
        // Filled rounded corner style
        button.backgroundColor = UIColor.init(red: 48/255, green: 173/255, blue: 99/255, alpha: 1)
        button.layer.cornerRadius = 25.0
        button.tintColor = UIColor.white
    }
    
    static func styleHollowButton(_ button:UIButton) {
        
        // Hollow rounded corner style
        button.layer.borderWidth = 2
        button.layer.borderColor = UIColor.black.cgColor
        button.layer.cornerRadius = 25.0
        button.tintColor = UIColor.black
    }
    
    static func isPasswordValid(_ password : String) -> Bool {
        
        let passwordTest = NSPredicate(format: "SELF MATCHES %@", "^(?=.*[a-z])(?=.*[$@$#!%*?&])[A-Za-z\\d$@$#!%*?&]{8,}")
        return passwordTest.evaluate(with: password)
    }
    
}



LoginViewController.swift

import Firebase
import UIKit
import FirebaseAuth

class LoginViewController: UIViewController {
    
  
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var loginTextField: UIButton!
    @IBOutlet weak var errorLabel: UILabel!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setUpElements()
    }
    func setUpElements() {
        //Hide error label
        errorLabel.alpha = 0
        //Style the elements
        Utilities.styleTextField(emailTextField)
        Utilities.styleTextField(passwordTextField)
    }   
    @IBAction func loginTapped(_ sender: Any) {     
        //Create cleaned versions of the text field
        let email = emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        let password = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
        UserDefaults.standard.set("\(email)", forKey: "email")
        UserDefaults.standard.set("\(password)", forKey: "password")
        //signing in the user
        Auth.auth().signIn(withEmail: email, password: password) { (resukt, error) in
            if error != nil {
                //Couldn't sign in
                self.errorLabel.text = error!.localizedDescription
                self.errorLabel.alpha = 1
                
            } else {
                
                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                let homeViewController = storyboard.instantiateViewController(withIdentifier: "TabBarController") as! TabBarController
                homeViewController.modalPresentationStyle = .overFullScreen
                self.present(homeViewController, animated: true)
            }
        }
    }
}







LoginViewController.swift

import UIKit

class UserCell: UITableViewCell {
    @IBOutlet weak var nameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}



















AddViewController.swift

import FirebaseDatabase
import FirebaseStorage
import UIKit

class AddViewController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UITabBarControllerDelegate {
    // MARK: IBOUTLET
    @IBOutlet weak var myImageView: UIImageView!
    
    // MARK: IBACTION
    
    // MARK: Variables
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // databaseTest()
        
    }
    
    private func databaseTest() {
        
        var ref: DatabaseReference!
        ref = Database.database().reference()
        
        ref.setValue(["username": "test"])
    }
    
    private func uploadImageFile(_ image: UIImage) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let date = Date()
        let imageName = "image\(date).jpg"
        let imageRef = storageRef.child("images/\(imageName)")
        
        imageRef.putData((image.pngData())!)
    }
    
    @IBAction func importImage(_ sender: Any) {
        let image = UIImagePickerController()
        
        image.delegate = self
        image.sourceType = UIImagePickerController.SourceType.camera
        image.allowsEditing = false
        
        self.present(image, animated: true)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            myImageView.image = image
            uploadImageFile(image)
        }
        
        self.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func importImageGalery(_ sender: Any) {
        
        let image = UIImagePickerController()
        
        image.delegate = self
        image.sourceType = UIImagePickerController.SourceType.savedPhotosAlbum
        image.allowsEditing = false
        
        self.present(image, animated: true)
    }
    
    func imagePickerGaleryController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            myImageView.image = image
            uploadImageFile(image)
        }
        
        self.dismiss(animated: true, completion: nil)
    }
    
}












TabBarController.swift

import Firebase
import UIKit

class TabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

    }
    
}





HomeViewController.swift


import FirebaseDatabase
import FirebaseStorage
import FirebaseAuth
import UIKit

class HomeViewController: UIViewController, UITabBarControllerDelegate, UIImagePickerControllerDelegate & UINavigationControllerDelegate {
    // MARK: Variables
	@IBOutlet weak var tableView: UITableView!
	var previousIndex = 0
    var ref: DatabaseReference!
    let userID = Auth.auth().currentUser?.uid
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ref = Database.database().reference()
        // current user
        ref.child("users").child(userID!).observeSingleEvent(of: .value, with: { (snapshot) in
            // Get user value
            
            let value = snapshot.value as? NSDictionary
            
            // ...
        }) { (error) in
            print(error.localizedDescription)
        }
        // all users

		self.tableView.register(UINib(nibName: "HomeCell", bundle: nil), forCellReuseIdentifier: "HomeCell")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.tabBarController?.delegate = self
    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        if tabBarController.selectedIndex != 2 {
            previousIndex = tabBarController.selectedIndex
        } else {
            tabBarController.selectedIndex = previousIndex
            showAlertSheet()
        }
    }
    
    private func showAlertSheet() {
        let alertController = UIAlertController(title: "Upload Photo", message: "Select place", preferredStyle: .actionSheet)
        let importImage = UIAlertAction(title: "Import Image", style: .default, handler: { action in
            self.importImageGallery()
        })
        let cameraAction = UIAlertAction(title: "Camera", style: .default, handler: { action in
            self.importFromCamera()
        })
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(importImage)
        alertController.addAction(cameraAction)
        alertController.addAction(cancelAction)
        present(alertController, animated: true)
    }
    
    private func importFromCamera() {
        let image = UIImagePickerController()
        
        image.delegate = self
        image.sourceType = UIImagePickerController.SourceType.camera
        image.allowsEditing = false
        
        self.present(image, animated: true)
    }
    
    private func uploadImageFile(_ image: UIImage) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let date = Date()
        let imageName = "image\(date).jpg"
        let imageRef = storageRef.child("images/\(imageName)")
        
        imageRef.putData((image.pngData())!)
    }
    
    @IBAction func importImage(_ sender: Any) {
        let image = UIImagePickerController()
        
        image.delegate = self
        image.sourceType = UIImagePickerController.SourceType.camera
        image.allowsEditing = false
        
        self.present(image, animated: true)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            // myImageView.image = image
            uploadImageFile(image)
        }
        
        self.dismiss(animated: true, completion: nil)
    }
    
    
    func importImageGallery() {
        
        let image = UIImagePickerController()
        
        image.delegate = self
        image.sourceType = UIImagePickerController.SourceType.photoLibrary
        image.allowsEditing = false
        
        self.present(image, animated: true)
    }
    
    func imagePickerGaleryController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            // myImageView.image = image
            uploadImageFile(image)
        }
        
        self.dismiss(animated: true, completion: nil)
    }
}

extension HomeViewController: UITableViewDelegate, UITableViewDataSource {
	func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return 10
	}

	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		if let homeCell = tableView.dequeueReusableCell(withIdentifier: "HomeCell") as? HomeCell {
			homeCell.selectionStyle = .none
			homeCell.layoutIfNeeded()

			return homeCell
		}

		return UITableViewCell()
	}


}






ProfileViewController.swift


import FirebaseAuth
import FirebaseDatabase
import FirebaseStorage
import Kingfisher
import UIKit

class ProfileViewController: UIViewController {
    
    @IBOutlet weak var profileImageView: UIImageView!
    @IBOutlet weak var displayNameLabel: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var followButton: UIButton!
    @IBOutlet weak var logoutButton: UIButton!
    
    var ref: DatabaseReference!
    
    @IBAction func logOutButtonTapped(_ sender: Any) {
        UserDefaults.standard.removeObject(forKey: "email")
        UserDefaults.standard.removeObject(forKey: "password")
        
        let firebaseAuth = Auth.auth()
        do {
          try firebaseAuth.signOut()
           
        } catch let signOutError{
            print(signOutError)
        }
        let storyboard = UIStoryboard(name: "Authentication", bundle: nil)
        let vc = storyboard.instantiateViewController(identifier: "MainVC")
        vc.modalPresentationStyle = .fullScreen
        present(vc, animated: true)
    }
    
    var userUploads: [StorageReference?] = []
    var userName = ""
    var isFollowButtonHidden = true
    var logoutButtonHidden = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setView()
        ref = Database.database().reference()
        if !userName.isEmpty {
            displayNameLabel.text = userName
        }
        followButton.isHidden = isFollowButtonHidden
        logoutButton.isHidden = logoutButtonHidden
    }

    @IBAction func followButtonTapped(_ sender: Any) {
        guard let uid = Auth.auth().currentUser?.uid else {
            return
        }
        // TODO: self.ref.child("users/\(uid)/followed/test").setValue("true")
    }
}


extension ProfileViewController {
    
    private func setView() {
        setProfilePicture()
        setDisplayName()
    }
}

extension ProfileViewController {
    
    private func setProfilePicture() {
        if let imageURL = Auth.auth().currentUser?.photoURL {
            let imageData = try! Data(contentsOf: imageURL)
            profileImageView.image = UIImage(data: imageData)
        }
    }
    
    private func setDisplayName() {
        if let displayName = Auth.auth().currentUser?.displayName {
            displayNameLabel.text = displayName
        }
    }
}

extension ProfileViewController: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return userUploads.count
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UserUploadCell.ReuseIdentifier, for: indexPath) as? UserUploadCell {
            let storageRef = userUploads[indexPath.row]
            storageRef?.downloadURL { url, error in
                if let error = error {
                    print("error", error)
                } else {
                    if let url = url {
                        cell.setCell(url)
                    }
                }
            }
            
            return cell
        }
        
        
        return UICollectionViewCell()
    }
    
}

extension ProfileViewController: UICollectionViewDelegate {
    
}

















UserUploadCell.swift


import Kingfisher
import UIKit

class UserUploadCell: UICollectionViewCell {
    
    @IBOutlet weak var imageView: UIImageView!
    
    public static let ReuseIdentifier = "UserUploadCell"
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    public func setCell(_ url: URL?) {
        guard let imageLink = url else { return }
        self.imageView.kf.setImage(with: imageLink)
    }
    
}

RegisterViewController.swift

import FirebaseAuth
import Firebase
import FirebaseFirestore
import UIKit

class RegisterViewController: UIViewController {
    
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var sighUpButton: UIButton!
    @IBOutlet weak var errorLabel: UILabel!
    
    // MARK: Variables
    var ref: DatabaseReference!

    override func viewDidLoad() {
        super.viewDidLoad()

        setUpElements()
        ref = Database.database().reference()
    }
    func setUpElements() {
        
            // Hide the error label
            errorLabel.alpha = 0
        
            // Style the elements
            Utilities.styleTextField(firstNameTextField)
            Utilities.styleTextField(lastNameTextField)
            Utilities.styleTextField(emailTextField)
            Utilities.styleTextField(passwordTextField)
            Utilities.styleFilledButton(sighUpButton)
        }
        
        // Check the fields and validate that the data is correct. If everything is correct, this method returns nil. Otherwise, it returns the error message
        func validateFields() -> String? {
            
            // Check that all fields are filled in
            if firstNameTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == "" ||
                lastNameTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == "" ||
                emailTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == "" ||
                passwordTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines) == "" {
                
                return "Please fill in all fields."
            }
            
            // Check if the password is secure
            let cleanedPassword = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
            
            if Utilities.isPasswordValid(cleanedPassword) == false {
                // Password isn't secure enough
                return "Please make sure your password is at least 8 characters, contains a special character and a number."
            }
            
            return nil
        }
        

        @IBAction func signUpTapped(_ sender: Any) {
            
            // Validate the fields
            let error = validateFields()
            
            if error != nil {
                
                // There's something wrong with the fields, show error message
                showError(error!)
            }
            else {
                
                // Create cleaned versions of the data
                let firstName = firstNameTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
                let lastName = lastNameTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
                let email = emailTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
                let password = passwordTextField.text!.trimmingCharacters(in: .whitespacesAndNewlines)
                
                // Create the user
                Auth.auth().createUser(withEmail: email, password: password) { (result, err) in
                    
                    // Check for errors
                    if err != nil {
                        
                        // There was an error creating the user
                        self.showError("Error creating user")
                    }
                    else {
                        self.ref.child("users/\(result!.user.uid)/firstName").setValue(firstName)
                        self.ref.child("users/\(result!.user.uid)/lastName").setValue(lastName)
                        self.ref.child("users/\(result!.user.uid)/email").setValue(email.lowercased())
                        // User was created successfully, now store the first name and last name
                        let db = Firestore.firestore()
                        
                        
                        db.collection("Users").addDocument(data: ["firstname":firstName, "lastname":lastName, "uid": result!.user.uid ]) { (error) in
                            
                            if error != nil {
                                // Show error message
                                self.showError("Error saving user data")
                            }
                        }
                        UserDefaults.standard.set("\(email)", forKey: "email")
                        UserDefaults.standard.set("\(password)", forKey: "password")
                        // Transition to the home screen
                        self.transitionToHome()
                    }
                }
            }
        }
        
        func showError(_ message:String) {
            
            errorLabel.text = message
            errorLabel.alpha = 1
        }
        
        func transitionToHome() {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let homeViewController = storyboard.instantiateViewController(withIdentifier: "TabBarController") as! TabBarController
            homeViewController.modalPresentationStyle = .overFullScreen
            self.present(homeViewController, animated: true)
        }
        
    }
MainVC.swift

import UIKit
import FirebaseAuth

class MainVC: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        guard let email = UserDefaults.standard.string(forKey: "email") else {
            return
        }
        
        guard let password = UserDefaults.standard.string(forKey: "password") else {
            return
        }
        
        if !email.isEmpty && !password.isEmpty {
            Auth.auth().signIn(withEmail: UserDefaults.standard.string(forKey: "email")!, password: UserDefaults.standard.string(forKey: "password")!) { (result, error) in
                if error != nil {
                    //Couldn't sign in
                    
                } else {
                    let storyboard = UIStoryboard(name: "Main", bundle: nil)
                    let homeViewController = storyboard.instantiateViewController(withIdentifier: "TabBarController") as! TabBarController
                    homeViewController.modalPresentationStyle = .overFullScreen
                    self.present(homeViewController, animated: true)
                }
            }
        }
    }
}



SearchViewController.swift

import UIKit
import Firebase

class SearchViewController: UIViewController {
    @IBOutlet weak var searchTextField: UITextField!
    @IBOutlet weak var tableView: UITableView!
    
    var ref: DatabaseReference!
    var dataSource = [String]()
    var keyword = ""

    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    @IBAction func searchButtonTapped(_ sender: Any) {
        keyword = searchTextField.text ?? ""
        searchByKeyword(keyword)
    }
    
    private func searchByKeyword(_ keyword: String) {
        dataSource = [String]()
        ref = Database.database().reference()
        ref.child("users").queryOrdered(byChild: "email").queryStarting(atValue: "\(keyword)").queryEnding(atValue: "\(keyword)" + "\u{f8ff}").observeSingleEvent(of: .value, with: { snapshot in
            if let result = snapshot.value as? [String: [String: String]] {
                for user in result {
                    if let firstName = user.value["firstName"], let lastName = user.value["lastName"] {
                        let data = "\(firstName) \(lastName)"
                        self.dataSource.append(data)
                        self.tableView.reloadData()
                    }
                }
            }

        })
        self.view.endEditing(true)
    }
}

extension SearchViewController: UITableViewDelegate {
    
}

extension SearchViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        dataSource.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "UserCell") as! UserCell
        cell.nameLabel.text = dataSource[indexPath.row]
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let profileViewController = storyboard.instantiateViewController(withIdentifier: "ProfileViewController") as? ProfileViewController {
            DispatchQueue.main.async {
                profileViewController.userName = self.dataSource[indexPath.row]
                profileViewController.modalPresentationStyle = .popover
                profileViewController.isFollowButtonHidden = false
                profileViewController.logoutButtonHidden = true
                self.present(profileViewController, animated: true)
            }
        }
        
    }
}

extension SearchViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField.text != "" {
            keyword = textField.text ?? ""
        } else {
            
        }
    }
}








NotificationViewController.swift

import UIKit

class NotificationViewController: UIViewController {
	@IBOutlet weak var tableView: UITableView!

    override func viewDidLoad() {
        super.viewDidLoad()

		self.tableView.register(UINib(nibName: "NotificationCell", bundle: nil), forCellReuseIdentifier: "NotificationCell")
    }

}

extension NotificationViewController: UITableViewDataSource, UITableViewDelegate {
	func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		return 10
	}

	func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		if let notificationCell = tableView.dequeueReusableCell(withIdentifier: "NotificationCell") as? NotificationCell {
			notificationCell.selectionStyle = .none
			notificationCell.layoutIfNeeded()

			return notificationCell
		}

		return UITableViewCell()
	}

	
}











HomeCell.swift


import UIKit

class HomeCell: UITableViewCell {

	@IBOutlet weak var profileImageView: UIImageView!

    override func awakeFromNib() {
        super.awakeFromNib()

		profileImageView.layer.borderWidth = 1.0
		profileImageView.layer.masksToBounds = false
		profileImageView.layer.borderColor = UIColor.white.cgColor
		profileImageView.layer.cornerRadius = profileImageView.layer.frame.size.width / 2
		profileImageView.clipsToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}

NotificationCell.swift

import UIKit

class NotificationCell: UITableViewCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}

