package io.gridbug.cats.motcat;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class StoreController {

    @Autowired
    private RedisTemplate<String, String> template;
	
	@RequestMapping(value="/store", method=RequestMethod.GET)
	public String uploadGet(@RequestParam Map<String, String> params) {
		return "num entries: " + template.boundListOps("motcat:raw-images").size();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/store")
	public String handleFileUpload(@RequestParam("name") String name,
			   @RequestParam("file") MultipartFile file,
			   RedirectAttributes redirectAttributes) {
		if (!file.isEmpty()) {
			try {
				
				template.boundListOps("motcat:raw-images").rightPush(Base64.getEncoder().encodeToString(file.getBytes()));
				if (template.boundListOps("motcat:raw-images").size() > 100) {
					template.boundListOps("motcat:raw-images").leftPop();
				}
				
				redirectAttributes.addFlashAttribute("message",
						"You successfully uploaded " + name + "!");
			}
			catch (Exception e) {
				redirectAttributes.addFlashAttribute("message",
						"You failed to upload " + name + " => " + e.getMessage());
			}
		}
		else {
			redirectAttributes.addFlashAttribute("message",
					"You failed to upload " + name + " because the file was empty");
		}

		return "uploaded!\r\n";
	}
	
	@RequestMapping(value="/index/{ival}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> latest(@PathVariable("ival") long ival) {
		if (template.boundListOps("motcat:raw-images").size() > 0) {
			String img = template.boundListOps("motcat:raw-images").index(ival);
			byte[] bytes = Base64.getDecoder().decode(img);
			return ResponseEntity
					.ok()
					.contentLength(bytes.length)
					.contentType(MediaType.IMAGE_PNG)
					.body(new ByteArrayResource(bytes));
		}
		else return ResponseEntity
				.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.body(new ByteArrayResource("nothing here".getBytes()));
	}
	
}
