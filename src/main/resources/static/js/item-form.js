document.addEventListener("DOMContentLoaded", function () {
    const itemTypeSelect = document.getElementById("itemType");
    const dynamicFields = document.getElementById("dynamicFields");
    const submitButton = document.getElementById("submitButton");
    const resultDiv = document.getElementById("result");
    const typeInput = document.getElementById("type");
    const category = document.getElementById("category");
    const formFields = {
        Book: `
            <div class="form-group">
                <label for="author">Author:</label>
                <input type="text" id="author" name="author" class="form-control">
            </div>
            <div class="form-group">
                <label for="publisher">Publisher:</label>
                <input type="text" id="publisher" name="publisher" class="form-control">
            </div>
            <div class="form-group">
                <label for="publicationDate">Publication Date:</label>
                <input type="date" id="publicationDate" name="publicationDate" class="form-control">
            </div>
        `,
        Laptop: `
            <div class="form-group">
                <label for="processorLaptop">Processor:</label>
                <input type="text" id="processorLaptop" name="processorLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="ramLaptop">RAM:</label>
                <input type="text" id="ramLaptop" name="ramLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="storageLaptop">Storage:</label>
                <input type="text" id="storageLaptop" name="storageLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="graphicsLaptop">Graphics:</label>
                <input type="text" id="graphicsLaptop" name="graphicsLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="osLaptop">Operating System:</label>
                <input type="text" id="osLaptop" name="osLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="displayLaptop">Display:</label>
                <input type="text" id="displayLaptop" name="displayLaptop" class="form-control">
            </div>
            <div class="form-group">
                <label for="batteryLaptop">Battery:</label>
                <input type="text" id="batteryLaptop" name="batteryLaptop" class="form-control">
            </div>
        `,
        Clothes: `
            <div class="form-group">
                <label for="sizeClothes">Size:</label>
                <input type="number" id="sizeClothes" name="sizeClothes" class="form-control">
            </div>
            <div class="form-group">
                <label for="colorClothes">Color:</label>
                <input type="text" id="colorClothes" name="colorClothes" class="form-control">
            </div>
            <div class="form-group">
                <label for="materialClothes">Material:</label>
                <input type="text" id="materialClothes" name="materialClothes" class="form-control">
            </div>
            <div class="form-group">
                <label for="typeClothes">Type:</label>
                <input type="text" id="typeClothes" name="typeClothes" class="form-control">
            </div>
        `,
        MobilePhone: `
            <div class="form-group">
                <label for="screenSizeMobilePhone">Screen Size:</label>
                <input type="text" id="screenSizeMobilePhone" name="screenSizeMobilePhone" class="form-control">
            </div>
            <div class="form-group">
                <label for="cameraMobilePhone">Camera:</label>
                <input type="text" id="cameraMobilePhone" name="cameraMobilePhone" class="form-control">
            </div>
            <div class="form-group">
                <label for="batteryMobilePhone">Battery:</label>
                <input type="text" id="batteryMobilePhone" name="batteryMobilePhone" class="form-control">
            </div>
            <div class="form-group">
                <label for="storageMobilePhone">Storage:</label>
                <input type="text" id="storageMobilePhone" name="storageMobilePhone" class="form-control">
            </div>
            <div class="form-group">
                <label for="ramMobilePhone">RAM:</label>
                <input type="text" id="ramMobilePhone" name="ramMobilePhone" class="form-control">
            </div>
        `,
        Electronics: `
            <div class="form-group">
                <label for="powerConsumption">Power Consumption:</label>
                <input type="text" id="powerConsumption" name="powerConsumption" class="form-control">
            </div>
            <div class="form-group">
                <label for="productType">Product Type:</label>
                <input type="text" id="productType" name="productType" class="form-control">
            </div>
            <div class="form-group">
                <label for="capacity">Capacity:</label>
                <input type="text" id="capacity" name="capacity" class="form-control">
            </div>
            <div class="form-group">
                <label for="controlType">Control Type:</label>
                <input type="text" id="controlType" name="controlType" class="form-control">
            </div>
        `,
        Shoes: `
            <div class="form-group">
                <label for="sizeShoes">Size:</label>
                <input type="text" id="sizeShoes" name="sizeShoes" class="form-control">
            </div>
            <div class="form-group">
                <label for="colorShoes">Color:</label>
                <input type="text" id="colorShoes" name="colorShoes" class="form-control">
            </div>
            <div class="form-group">
                <label for="materialShoes">Material:</label>
                <input type="text" id="materialShoes" name="materialShoes" class="form-control">
            </div>
            <div class="form-group">
                <label for="typeShoes">Type:</label>
                <input type="text" id="typeShoes" name="typeShoes" class="form-control">
            </div>
        `
    };


    itemTypeSelect.addEventListener("change", function () {
        const selectedType = itemTypeSelect.value;
        typeInput.value = itemTypeSelect.value;
        dynamicFields.innerHTML = formFields[selectedType] || "";
    });

    submitButton.addEventListener("click", async function () {
        const form = document.getElementById("itemForm");
        category.value = itemTypeSelect.value;


        const formData = new FormData(form);

        // Chuyển đổi FormData thành JSON
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        try {
            const response = await fetch("/api/items", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",  // Chỉ định kiểu dữ liệu là JSON
                },
                body: JSON.stringify(data),  // Gửi dữ liệu dưới dạng JSON
            });

            if (response.ok) {
                const result = await response.json();
                resultDiv.innerHTML = `<p class="success">Saved successfully! Product ID: ${result.id}</p>`;
            } else {
                const error = await response.text();
                resultDiv.innerHTML = `<p class="error">Error: ${error}</p>`;
            }
        } catch (error) {
            resultDiv.innerHTML = `<p class="error">Network Error: ${error.message}</p>`;
        }
    });
});
